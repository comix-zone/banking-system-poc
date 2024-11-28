package com.dualsoft.banking.account.service.clients;

import com.dualsoft.banking.account.service.dto.AccountTransactionListPageResponseDTO;
import com.dualsoft.banking.account.service.dto.transaction.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.UUID;

@HttpExchange(url = "/v1/transaction")
public interface TransactionClient {

    @GetExchange("/{userId}/balance")
    TransactionBalanceResponseDTO getBalance(@PathVariable UUID userId);

    @PostExchange("/transfer")
    List<TransactionTransferMoneyResponseDTO> transfer(@RequestBody TransactionTransferMoneyRequestDTO request);
    @PostExchange("/transfer-multi")
    List<TransactionTransferMoneyResponseDTO> transferMulti(@RequestBody TransactionTransferMoneyMultiRequestDTO request);

    @PostExchange("/initialize-wallet")
    TransactionTransferMoneyResponseDTO initialize(@RequestBody TransactionInitializeAccountRequestDTO request);

    @GetExchange("/{accountId}")
    AccountTransactionListPageResponseDTO getTransactions(
            @PathVariable UUID accountId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort
            );
}
