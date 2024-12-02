//package com.dualsoft.banking.transaction.service;
//
//import com.dualsoft.banking.transaction.dto.InitializeWalletRequestDTO;
//import com.dualsoft.banking.transaction.dto.TransferMoneyRequestDTO;
//import com.dualsoft.banking.transaction.dto.TransferMoneyResponseDTO;
//import com.dualsoft.banking.transaction.exception.TransactionDoubleInitializationException;
//import com.dualsoft.banking.transaction.exception.TransactionUserNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//public class TransactionServiceTest {
//    @Autowired
//    private TransactionService service;
//
//    @Test
//    public void testConcurrentMoneyTransfer() throws InterruptedException, ExecutionException, ExecutionException, TransactionDoubleInitializationException, TransactionUserNotFoundException {
//        //Init wallet
//        UUID fromAccountId = UUID.randomUUID();
//        UUID toAccountId1  = UUID.randomUUID();
//        UUID toAccountId2  = UUID.randomUUID();
//
//        InitializeWalletRequestDTO initFromUser = InitializeWalletRequestDTO.builder()
//                .accountId(fromAccountId)
//                .amount(BigDecimal.valueOf(10000))
//                .currency("RSD")
//                .build();
//        service.initializeWallet(initFromUser);
//
//
//        InitializeWalletRequestDTO initToUser1 = InitializeWalletRequestDTO.builder()
//                .accountId(toAccountId1)
//                .amount(BigDecimal.valueOf(0))
//                .currency("RSD")
//                .build();
//        service.initializeWallet(initToUser1);
//
//        InitializeWalletRequestDTO initToUser2 = InitializeWalletRequestDTO.builder()
//                .accountId(toAccountId2)
//                .amount(BigDecimal.valueOf(0))
//                .currency("RSD")
//                .build();
//        service.initializeWallet(initToUser2);
//
//        BigDecimal transferAmount = new BigDecimal("50.00");
//        String currency = "RSD";
//        var correlationId1 = UUID.randomUUID();
//        var correlationId2 = UUID.randomUUID();
//        // Prepare request DTOs
//        TransferMoneyRequestDTO request1 = new TransferMoneyRequestDTO();
//        request1.setFromAccountId(fromAccountId);
//        request1.setToAccountId(toAccountId1);
//        request1.setCorrelationId(correlationId1);
//        request1.setAmount(transferAmount);
//        request1.setCurrency(currency);
//
//        TransferMoneyRequestDTO request2 = new TransferMoneyRequestDTO();
//        request2.setFromAccountId(fromAccountId);
//        request2.setToAccountId(toAccountId2);
//        request2.setAmount(transferAmount);
//        request2.setCorrelationId(correlationId2);
//        request2.setCurrency(currency);
//
//        // Executor service for concurrent execution
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        // Create tasks
//        Callable<List<TransferMoneyResponseDTO>> task1 = () -> service.transferMoney(request1);
//        Callable<List<TransferMoneyResponseDTO>> task2 = () -> service.transferMoney(request2);
//
//
//        // Submit tasks for concurrent execution
//        List<Future<List<TransferMoneyResponseDTO>>> futures = new ArrayList<>();
//        futures.add(executor.submit(task1));
//        futures.add(executor.submit(task2));
//
//        // Wait for results
//        List<TransferMoneyResponseDTO> result1 = futures.get(0).get();
//        List<TransferMoneyResponseDTO> result2 = futures.get(1).get();
//
//
//        executor.shutdown();
//
//        var balanceFrom = service.getUserBalance(fromAccountId);
//        var balanceTo1  = service.getUserBalance(toAccountId1);
//        var balanceTo2  = service.getUserBalance(toAccountId2);
//
//        // Assert results
//        assertEquals(9900,balanceFrom.getBalance().intValue(), "balance of from account does not match");
//        assertEquals(50, balanceTo1.getBalance().intValue(), "balance of to1 account does not match");
//        assertEquals(50, balanceTo2.getBalance().intValue(), "balance of to2 account does not match");
//    }
//}
