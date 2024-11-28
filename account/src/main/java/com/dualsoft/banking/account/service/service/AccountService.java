package com.dualsoft.banking.account.service.service;

import com.dualsoft.banking.account.service.clients.TransactionClient;
import com.dualsoft.banking.account.service.domain.AccountOwner;
import com.dualsoft.banking.account.service.domain.AccountStatus;
import com.dualsoft.banking.account.service.dto.*;
import com.dualsoft.banking.account.service.dto.transaction.*;
import com.dualsoft.banking.account.service.exception.*;
import com.dualsoft.banking.account.service.repository.AccountOwnerRepository;
import com.dualsoft.banking.events.domain.account.AccountEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final KafkaTemplate<String, AccountEvent> kafkaTemplate;

    private final AccountOwnerRepository accountRepository;

    private final TransactionClient transactionClient;
    private final ModelMapper modelMapper = new ModelMapper();
    @Transactional
    public CreateAccountResponseDTO createAccount(CreateAccountRequestDTO request) throws AccountAlreadyExistsException {

        Optional<AccountOwner> accountOwner = accountRepository.findByPersonalIdOrEmailOrPhone(request.getPersonalId(), request.getEmail(), request.getPhone());

        if (accountOwner.isPresent()) {
            throw new AccountAlreadyExistsException();
        }

        AccountOwner newAccountOwner = AccountOwner.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .surname(request.getSurname())
                .personalId(request.getPersonalId())
                .email(request.getEmail())
                .phone(request.getPhone())
                .accountStatus(AccountStatus.ACTIVE.getStatusCode()) // TODO: We could set status to PENDING so it awaits PIN confirmation
                .address(request.getAddress())
                .currency(request.getCurrency())
                .build();

        //In case this fails user should not be saved
        //for now err 500 is returned
        var result = transactionClient.initialize(TransactionInitializeAccountRequestDTO.builder()
                        .accountId(newAccountOwner.getId())
                        .amount(request.getDepositAmount())
                        .currency(newAccountOwner.getCurrency())
                    .build());

        var addedUser =  accountRepository.saveAndFlush(newAccountOwner);

        //TODO: publish an event to notification service
        //      So that nofication service can save information for future notification
        //      like email, sms, push notification etc.
        //      On creation send AccountCreatedEvent so it notifies the user about the account creation
        //      Or sends a welcome email to the user
        //      Or sends a PIN to his phone number

        AccountEvent accountCreatedEvent = AccountEvent.builder()
                .id(addedUser.getId())
                .personalId(addedUser.getPersonalId())
                .name(addedUser.getName())
                .surname(addedUser.getSurname())
                .email(addedUser.getEmail())
                .phone(addedUser.getPhone())
                .address(addedUser.getAddress())
                .accountStatus(addedUser.getAccountStatus())
                .currency(addedUser.getCurrency())
                .build();

        kafkaTemplate.send("account-created-topic", accountCreatedEvent);

        return CreateAccountResponseDTO.builder()
                .id(addedUser.getId())
                .name(addedUser.getName())
                .surname(addedUser.getSurname())
                .email(addedUser.getEmail())
                .phone(addedUser.getPhone())
                .address(addedUser.getAddress())
                .currency(addedUser.getCurrency())
                .balance(result.getBalance())
                .build();
    }

    @Transactional
    public boolean deleteAccount(UUID accountId) throws AccountNotFoundException, AccountPreconditionException, AccountGeneralError {
        AccountDetailsResponseDTO accountDetails = this.getAccountDetails(accountId);

        if (accountDetails.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new AccountPreconditionException("Remove all funds from account before deleting it");
        }

        if (accountDetails.getAccountStatus() == AccountStatus.DELETED.getStatusCode()) {
            throw new AccountNotFoundException();
        }

        // Account is marked as deleted but in fact we could mark it as AccountStatus.PENDING_DELETION and
        // send notification or confirmation to the user so only after its confirmed we perform the deletion
        var deleted = accountRepository.setAccountStatus(accountId, AccountStatus.DELETED.getStatusCode());

        if (deleted == 0) {
            throw new AccountGeneralError();
        }

        AccountEvent accountDeletedEvent = AccountEvent.builder()
                .id(accountDetails.getId())
                .personalId(accountDetails.getPersonalId())
                .name(accountDetails.getName())
                .surname(accountDetails.getSurname())
                .email(accountDetails.getEmail())
                .phone(accountDetails.getPhone())
                .address(accountDetails.getAddress())
                .accountStatus(accountDetails.getAccountStatus())
                .currency(accountDetails.getCurrency())
                .build();
        kafkaTemplate.send("account-deleted-topic", accountDeletedEvent);

        return true;
    }

    public AccountDetailsResponseDTO getAccountDetails(UUID accountId) throws AccountNotFoundRuntimeException {
        var accountOwner = accountRepository.findById(accountId).orElseThrow(AccountNotFoundRuntimeException::new);

        TransactionBalanceResponseDTO userBalance = transactionClient.getBalance(accountId);

        return AccountDetailsResponseDTO.builder()
                .id(accountOwner.getId())
                .name(accountOwner.getName())
                .surname(accountOwner.getSurname())
                .email(accountOwner.getEmail())
                .phone(accountOwner.getPhone())
                .address(accountOwner.getAddress())
                .currency(accountOwner.getCurrency())
                .balance(userBalance.getBalance())
                .personalId(accountOwner.getPersonalId())
                .accountStatus(accountOwner.getAccountStatus())
                .build();
    }

    public List<TransferMoneyResponseDTO> transferMoney(AccountTransferMoneyRequestDTO request) throws AccountNotFoundException, AccountPreconditionException {
        var accountFrom = accountRepository.findById(request.getFromAccountId());

        if (accountFrom.isEmpty()) {
            throw new AccountNotFoundException("Sender account not found");
        }

        var accountTo = accountRepository.findById(request.getToAccountId());

        if (accountTo.isEmpty()) {
            throw new AccountNotFoundException("Receiver account not found");
        }

        if (accountFrom.get().getAccountStatus() != AccountStatus.ACTIVE.getStatusCode()) {
            throw new AccountPreconditionException("Sender account is not active");
        }

        if (accountTo.get().getAccountStatus() != AccountStatus.ACTIVE.getStatusCode()) {
            throw new AccountPreconditionException("Receiver account is not active");
        }

        var transactionResponse = transactionClient.transfer(
                TransactionTransferMoneyRequestDTO.builder()
                    .fromAccountId(request.getFromAccountId())
                    .toAccountId(request.getToAccountId())
                    .correlationId(request.getCorrelationId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .build()
        );

        return transactionResponse.stream().map(r -> modelMapper.map(r, TransferMoneyResponseDTO.class)).collect(Collectors.toList());
    }

    public List<TransferMoneyResponseDTO> transferMoneyMulti(AccountTransferMoneyMultiRequestDTO request) throws AccountNotFoundException, AccountPreconditionException {
        var accountFrom = accountRepository.findById(request.getFromAccountId()).orElseThrow(AccountNotFoundException::new);

        if (accountFrom.getAccountStatus() != AccountStatus.ACTIVE.getStatusCode()) {
            throw new AccountPreconditionException("Sender account is not active");
        }

        var recipients = request.getRecipients();
        List<TransactionTransferRecipientDTO> transferRecipientDTOS = new ArrayList<>();
        recipients.forEach(recipient -> {
            var accountTo = accountRepository.findById(recipient.getToAccountId()).orElseThrow(AccountNotFoundRuntimeException::new);
            if (accountTo.getAccountStatus() != AccountStatus.ACTIVE.getStatusCode()) {
                throw new AccountPreconditionRuntimeException("Receiver account is not active: " + accountTo.getId());
            }
            transferRecipientDTOS.add(
                    TransactionTransferRecipientDTO.builder()
                            .toAccountId(recipient.getToAccountId())
                            .amount(recipient.getAmount())
                            .currency(recipient.getCurrency())
                            .build()
            );
        });

        var transactionResponse = transactionClient.transferMulti(
                TransactionTransferMoneyMultiRequestDTO.builder()
                    .fromAccountId(request.getFromAccountId())
                    .correlationId(request.getCorrelationId())
                    .recipients(transferRecipientDTOS)
                    .build()
        );

        return transactionResponse.stream().map(
                r -> modelMapper.map(r, TransferMoneyResponseDTO.class)
        ).collect(Collectors.toList());
    }
    @Transactional
    public UpdateAccountResponseDTO updateAccount(UUID accountId, UpdateAccountRequestDTO request) throws AccountNotFoundException {
        var optionalAccount = accountRepository.findById(accountId);
        if(optionalAccount.isEmpty()) {
            throw new AccountNotFoundException();
        }

        var account = optionalAccount.get();
        account.setName(request.getName());
        account.setSurname(request.getSurname());
        account.setEmail(request.getEmail());
        account.setAddress(request.getAddress());
        account.setPhone(request.getPhone());
        var updatedAccount = accountRepository.save(account);

        AccountEvent accountUpdatedEvent = AccountEvent.builder()
                .id(updatedAccount.getId())
                .personalId(updatedAccount.getPersonalId())
                .name(updatedAccount.getName())
                .surname(updatedAccount.getSurname())
                .email(updatedAccount.getEmail())
                .phone(updatedAccount.getPhone())
                .address(updatedAccount.getAddress())
                .accountStatus(updatedAccount.getAccountStatus())
                .currency(updatedAccount.getCurrency())
                .build();

        kafkaTemplate.send("account-updated-topic", accountUpdatedEvent);

        return UpdateAccountResponseDTO.builder()
                .id(updatedAccount.getId())
                .accountStatus(updatedAccount.getAccountStatus())
                .address(updatedAccount.getAddress())
                .name(updatedAccount.getName())
                .surname(updatedAccount.getSurname())
                .phone(updatedAccount.getPhone())
                .email(updatedAccount.getEmail())
                .currency(updatedAccount.getCurrency())
                .build();
    }

    public AccountTransactionListPageResponseDTO getAccountTransactions(UUID accountId, int page, int size, String sort) throws AccountNotFoundException {
        var account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        return transactionClient.getTransactions(accountId, page, size, sort);
    }
}
