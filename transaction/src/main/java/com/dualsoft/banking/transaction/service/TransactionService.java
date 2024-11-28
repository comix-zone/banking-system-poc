package com.dualsoft.banking.transaction.service;

import com.dualsoft.banking.events.domain.transaction.TransactionEvent;
import com.dualsoft.banking.transaction.domain.Transaction;
import com.dualsoft.banking.transaction.domain.TransactionAdvisoryLockKey;
import com.dualsoft.banking.transaction.domain.TransactionType;
import com.dualsoft.banking.transaction.dto.*;
import com.dualsoft.banking.transaction.exception.*;
import com.dualsoft.banking.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    @Transactional
    public List<TransferMoneyResponseDTO> transferMoney(TransferMoneyRequestDTO request) throws TransactionInsufficientFundsException, TransactionGeneralErrorException, TransactionUserNotFoundException {
        try {

            var correlationId = request.getCorrelationId();

            var advisoryLockKeyCorrelation = new TransactionAdvisoryLockKey(request.getCorrelationId());
            final long lockKeyCorrelation = advisoryLockKeyCorrelation.generateHashKey();

            var advisoryLockKeyFrom = new TransactionAdvisoryLockKey(request.getFromAccountId());
            final long lockKeyFrom = advisoryLockKeyFrom.generateHashKey();

            var advisoryLockKeyTo = new TransactionAdvisoryLockKey(request.getToAccountId());
            final long lockKeyTo = advisoryLockKeyTo.generateHashKey();

            transactionRepository.lockTransaction(lockKeyFrom);
            transactionRepository.lockTransaction(lockKeyTo);
            transactionRepository.lockTransaction(lockKeyCorrelation);

            var idempotentTransactions = transactionRepository.findAllByCorrelationId(correlationId);
            if(!idempotentTransactions.isEmpty()) {
                return idempotentTransactions.stream().map(trans -> {
                    log.info("Idempotency triggered");
                    var lastTransaction = transactionRepository.getLastTransaction(trans.getAccountId())
                            .orElseThrow(TransactionNotFoundRuntimeException::new);

                    TransferMoneyResponseDTO responseDTO = TransferMoneyResponseDTO.builder()
                            .transactionId(trans.getTransactionId())
                            .correlationId(trans.getCorrelationId())
                            .balance(lastTransaction.getCurrentBalance())
                            .currency(trans.getCurrency())
                            .build();
                    return responseDTO;
                }).collect(Collectors.toList());
            }

            var lastTransactionFrom = transactionRepository.getLastTransaction(request.getFromAccountId())
                    .orElseThrow(TransactionUserNotFoundException::new);

            var balanceFrom = lastTransactionFrom.getCurrentBalance();
            var newBalance = balanceFrom.subtract(request.getAmount());

            if (newBalance.compareTo(BigDecimal.ZERO) < 0){
                throw new TransactionInsufficientFundsException();
            }

            Transaction transactionFrom = Transaction.builder()
                    .accountId(request.getFromAccountId())
                    .amount(request.getAmount())
                    .transactionType(TransactionType.WITHDRAWAL.getValue())
                    .currency(request.getCurrency())
                    .transactionId(UUID.randomUUID())
                    .correlationId(correlationId)
                    .currentBalance(newBalance)
                    .build();

            var lastTransactionTo  =  transactionRepository.getLastTransaction(request.getToAccountId())
                    .orElseThrow(TransactionUserNotFoundException::new);

            var balanceTo = lastTransactionTo.getCurrentBalance();
            var newBalanceTo = balanceTo.add(request.getAmount());

            Transaction transactionTo = Transaction.builder()
                    .accountId(request.getToAccountId())
                    .amount(request.getAmount())
                    .transactionType(TransactionType.DEPOSIT.getValue())
                    .currency(request.getCurrency())
                    .transactionId(UUID.randomUUID())
                    .correlationId(correlationId)
                    .currentBalance(newBalanceTo)
                    .build();

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transactionFrom);
            transactions.add(transactionTo);

            var result = transactionRepository.saveAllAndFlush(transactions);

            return result.stream().map(trans -> {
                TransferMoneyResponseDTO responseDTO = TransferMoneyResponseDTO.builder()
                        .transactionId(trans.getTransactionId())
                        .correlationId(trans.getCorrelationId())
                        .balance(trans.getCurrentBalance())
                        .currency(trans.getCurrency())
                        .build();

                TransactionEvent event = TransactionEvent.builder()
                        .executedAt(trans.getCreatedAt())
                        .accountId(trans.getAccountId())
                        .correlationTransactionId(correlationId)
                        .localTransactionId(trans.getTransactionId())
                        .amount(trans.getAmount())
                        .type(TransactionType.getNameByValue(trans.getTransactionType()))
                        .balance(trans.getCurrentBalance())
                        .build();
                kafkaTemplate.send("transaction-executed-topic", event);

                return responseDTO;
            }).collect(Collectors.toList());
        } catch (JpaSystemException ex) {
            throw new TransactionInsufficientFundsException();
        } catch (DataIntegrityViolationException ex) {
            throw new TransactionUserNotFoundException();
        } catch (TransactionInsufficientFundsException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TransactionGeneralErrorException(ex.getMessage());
        }
    }
    @Transactional
    public List<TransferMoneyResponseDTO> transferMoneyMulti(TransferMoneyMultiRequestDTO request) throws TransactionInsufficientFundsException, TransactionGeneralErrorException, TransactionUserNotFoundException {
        try {

            var correlationId = request.getCorrelationId();

            var advisoryLockKeyCorrelation = new TransactionAdvisoryLockKey(request.getCorrelationId());
            var lockKeyCorrelation = advisoryLockKeyCorrelation.generateHashKey();
            transactionRepository.lockTransaction(lockKeyCorrelation);

            var advisoryLockKeyFrom = new TransactionAdvisoryLockKey(request.getFromAccountId());
            final long lockKeyFrom = advisoryLockKeyFrom.generateHashKey();
            transactionRepository.lockTransaction(lockKeyFrom);

            var idempotentTransactions = transactionRepository.findAllByCorrelationId(correlationId);
            if(!idempotentTransactions.isEmpty()) {
                return idempotentTransactions.stream().map(trans -> {
                    log.info("Idempotency triggered");
                    var lastTransaction = transactionRepository.getLastTransaction(trans.getAccountId()).orElseThrow(TransactionNotFoundRuntimeException::new);

                    TransferMoneyResponseDTO responseDTO = TransferMoneyResponseDTO.builder()
                            .transactionId(trans.getTransactionId())
                            .correlationId(trans.getCorrelationId())
                            .balance(lastTransaction.getCurrentBalance())
                            .currency(trans.getCurrency())
                            .build();
                    return responseDTO;
                }).collect(Collectors.toList());
            }

            var recipients = request.getRecipients();
            final BigDecimal[] totalBalanceToDeduct = {BigDecimal.ZERO};
            List<Transaction> transactions = new ArrayList<>();

            recipients.forEach(recipient -> {
                totalBalanceToDeduct[0] = totalBalanceToDeduct[0].add(recipient.getAmount());
                final var advisoryLockKeyTo = new TransactionAdvisoryLockKey(recipient.getToAccountId());
                final long lockKeyTo = advisoryLockKeyTo.generateHashKey();
                transactionRepository.lockTransaction(lockKeyTo);
                final Transaction lastTransactionTo;
                lastTransactionTo = transactionRepository.getLastTransaction(recipient.getToAccountId()).orElseThrow(TransactionNotFoundRuntimeException::new);
                final var balanceTo = lastTransactionTo.getCurrentBalance();
                final var newBalanceTo = balanceTo.add(recipient.getAmount());

                Transaction transactionTo = Transaction.builder()
                        .accountId(recipient.getToAccountId())
                        .amount(recipient.getAmount())
                        .transactionType(TransactionType.DEPOSIT.getValue())
                        .currency(recipient.getCurrency())
                        .transactionId(UUID.randomUUID())
                        .correlationId(correlationId)
                        .currentBalance(newBalanceTo)
                        .build();
                transactions.add(transactionTo);
            });


            var lastFromTransaction = transactionRepository.getLastTransaction(request.getFromAccountId()).orElseThrow(TransactionUserNotFoundException::new);
            var currentBalanceFrom = lastFromTransaction.getCurrentBalance();
            var newBalance = currentBalanceFrom.subtract(totalBalanceToDeduct[0]);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new TransactionInsufficientFundsException();
            }

            Transaction transactionFrom = Transaction.builder()
                    .accountId(request.getFromAccountId())
                    .amount(totalBalanceToDeduct[0])
                    .transactionType(TransactionType.WITHDRAWAL.getValue())
                    .currency(lastFromTransaction.getCurrency()) // @TODO: currency is not currently implemented
                    .transactionId(UUID.randomUUID())
                    .correlationId(correlationId)
                    .currentBalance(newBalance)
                    .build();


            transactions.add(transactionFrom);

            var result = transactionRepository.saveAllAndFlush(transactions);

            return result.stream().map(trans -> {
                TransferMoneyResponseDTO responseDTO = TransferMoneyResponseDTO.builder()
                        .transactionId(trans.getTransactionId())
                        .balance(trans.getCurrentBalance())
                        .currency(trans.getCurrency())
                        .correlationId(correlationId)
                        .build();
                //TODO: Improve logic so sender knows who sent money
                //      Tho when listing transactions he can find it

                TransactionEvent event = TransactionEvent.builder()
                        .executedAt(trans.getCreatedAt())
                        .accountId(trans.getAccountId())
                        .correlationTransactionId(correlationId)
                        .localTransactionId(trans.getTransactionId())
                        .amount(trans.getAmount())
                        .type(TransactionType.getNameByValue(trans.getTransactionType()))
                        .balance(trans.getCurrentBalance())
                        .build();
                kafkaTemplate.send("transaction-executed-topic", event);

                return responseDTO;
            }).collect(Collectors.toList());
        } catch (JpaSystemException ex) {
            throw new TransactionInsufficientFundsException();
        } catch (DataIntegrityViolationException ex) {
            throw new TransactionUserNotFoundException();
        } catch (Exception ex) {
            throw new TransactionGeneralErrorException(ex.getMessage());
        }
    }
    public BalanceResponseDTO getUserBalance(UUID accountId) throws TransactionUserNotFoundException {
        var advisoryLockKey = new TransactionAdvisoryLockKey(accountId);
        var lockKey = advisoryLockKey.generateHashKey();
        transactionRepository.lockTransaction(lockKey);

        Transaction lastTransaction = transactionRepository.getLastTransaction(accountId).orElseThrow(TransactionUserNotFoundException::new);

        return BalanceResponseDTO.builder()
                .balance(lastTransaction.getCurrentBalance())
                //TODO: add currency check
                .currency(lastTransaction.getCurrency())
                .build();
    }

    @Transactional
    public TransferMoneyResponseDTO initializeWallet(InitializeWalletRequestDTO accountRequest) throws TransactionDoubleInitializationException {

        var optionalLastTransaction = transactionRepository.getLastTransaction(accountRequest.getAccountId());

        if(optionalLastTransaction.isPresent()){
            var lastTransaction = optionalLastTransaction.get();
            log.warn("User wallet already initialized, returning last balance");
            return TransferMoneyResponseDTO.builder()
                    .currency(lastTransaction.getCurrency())
                    .transactionId(lastTransaction.getTransactionId())
                    .correlationId(lastTransaction.getCorrelationId())
                    .balance(lastTransaction.getCurrentBalance())
                    .build();
        }

        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID())
                .accountId(accountRequest.getAccountId())
                .transactionType(TransactionType.DEPOSIT.getValue())
                .amount(accountRequest.getAmount())
                .correlationId(UUID.randomUUID())
                .currentBalance(accountRequest.getAmount())
                .currency(accountRequest.getCurrency())
                .build();
        var result = transactionRepository.save(transaction);

        TransactionEvent event = TransactionEvent.builder()
                .executedAt(result.getCreatedAt())
                .accountId(result.getAccountId())
                .correlationTransactionId(result.getCorrelationId())
                .localTransactionId(result.getTransactionId())
                .amount(result.getAmount())
                .type(TransactionType.getNameByValue(result.getTransactionType()))
                .balance(result.getCurrentBalance())
                .build();

        kafkaTemplate.send("transaction-executed-topic", event);

        return TransferMoneyResponseDTO.builder()
                .transactionId(result.getTransactionId())
                .correlationId(result.getCorrelationId())
                .currency(transaction.getCurrency())
                .balance(result.getCurrentBalance())
                .build();
    }

    public TransactionListResponseDTO getTransactionList(UUID accountId, PageRequest pageRequest) {
        var transactionList = transactionRepository.findAllByAccountId(accountId, pageRequest);
        return TransactionListResponseDTO.builder()
                .data(transactionList.getContent())
                .page(transactionList.getNumber())
                .size(transactionList.getSize())
                .total(transactionList.getTotalElements())
                .sort(transactionList.getSort().toString())
                .build();
    }
}
