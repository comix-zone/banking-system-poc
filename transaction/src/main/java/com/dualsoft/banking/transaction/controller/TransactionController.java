package com.dualsoft.banking.transaction.controller;

import com.dualsoft.banking.transaction.dto.*;
import com.dualsoft.banking.transaction.exception.TransactionDoubleInitializationException;
import com.dualsoft.banking.transaction.exception.TransactionGeneralErrorException;
import com.dualsoft.banking.transaction.exception.TransactionInsufficientFundsException;
import com.dualsoft.banking.transaction.exception.TransactionUserNotFoundException;
import com.dualsoft.banking.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Operations for transactions")
@RequestMapping(value = "/v1/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @Operation(summary = "Execute a transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TransferMoneyResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "422", description = "Insufficient balance"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransferMoneyResponseDTO>> transferMoney(@RequestBody TransferMoneyRequestDTO request) throws TransactionGeneralErrorException, TransactionInsufficientFundsException, TransactionUserNotFoundException {
        return ResponseEntity.ok(transactionService.transferMoney(request));
    }

    @PostMapping("/transfer-multi")
    @Operation(summary = "Execute a list of transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TransferMoneyMultiRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "422", description = "Insufficient balance"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransferMoneyResponseDTO>> transferMoneyMulti(@RequestBody TransferMoneyMultiRequestDTO request) throws TransactionGeneralErrorException, TransactionInsufficientFundsException, TransactionUserNotFoundException {
        return ResponseEntity.ok(transactionService.transferMoneyMulti(request));
    }

    @GetMapping(path = "/{userId}/balance")
    @Operation(summary = "Get user balance")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BalanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BalanceResponseDTO> getUserBalance(@PathVariable UUID userId) throws TransactionUserNotFoundException {
        return ResponseEntity.ok(transactionService.getUserBalance(userId));
    }

    @PostMapping(path = "/initialize-wallet", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Initializes user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TransferMoneyResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        })
    public ResponseEntity<TransferMoneyResponseDTO> initializeWallet(@RequestBody InitializeWalletRequestDTO request) throws TransactionDoubleInitializationException {
        return ResponseEntity.ok(transactionService.initializeWallet(request));
    }

    @GetMapping(path = "/{accountId}")
    @Operation(summary = "Returns a list of transactions wrapped in data object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = TransactionListResponseDTO.class))),
    })
    public ResponseEntity<TransactionListResponseDTO> getTransactions(
            @PathVariable UUID accountId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "sort", defaultValue = "DESC", required = false) String sort
    ) {
        var direction = Sort.Direction.fromString(sort);
        PageRequest pageRequest = PageRequest.of(page, size, direction, "createdAt");
        return ResponseEntity.ok(transactionService.getTransactionList(accountId, pageRequest));
    }


}
