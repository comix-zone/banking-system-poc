package com.dualsoft.banking.account.service.controller;


import com.dualsoft.banking.account.service.dto.*;
import com.dualsoft.banking.account.service.exception.*;
import com.dualsoft.banking.account.service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v1/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Account Controller", description = "Operations pertaining to account")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create account", description = "Create account")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created successfully", content = @Content(schema = @Schema(implementation = CreateAccountResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Account already exists")
    })
    public ResponseEntity<CreateAccountResponseDTO> createAccount(@Validated @RequestBody CreateAccountRequestDTO createAccountRequestDTO) throws AccountAlreadyExistsException {
        return ResponseEntity.ok(accountService.createAccount(createAccountRequestDTO));
    }

    @DeleteMapping(path = "/{accountId}")
    @Operation(summary = "Delete account", description = "Delete account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "412", description = "Precondition failed"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity deleteAccount(@PathVariable UUID accountId) throws AccountNotFoundException, AccountPreconditionException, AccountGeneralError {
        accountService.deleteAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get account", description = "Get account Information")
    @GetMapping(path = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account details returned successfully", content = @Content(schema = @Schema(implementation = AccountDetailsResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountDetailsResponseDTO> getAccountDetails(@PathVariable UUID accountId) throws AccountNotFoundRuntimeException {
        return ResponseEntity.ok(accountService.getAccountDetails(accountId));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money", description = "Transfer money from one account to another")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MOney transferred successfully", content = @Content(schema = @Schema(implementation = TransferMoneyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "412", description = "Not Enough Balance"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransferMoneyResponseDTO>> transferMoney(@RequestBody AccountTransferMoneyRequestDTO request) throws AccountNotFoundException, AccountPreconditionException, AccountGeneralError {
        return ResponseEntity.ok(accountService.transferMoney(request));
    }

    @PostMapping("/transfer-multi")
    @Operation(summary = "Transfer money", description = "Transfer money from one account to multiple")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Money transferred successfully", content = @Content(schema = @Schema(implementation = TransferMoneyResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "412", description = "Not Enough Balance"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransferMoneyResponseDTO>> transferMoneyMulti(@RequestBody AccountTransferMoneyMultiRequestDTO request) throws AccountNotFoundException, AccountPreconditionException, AccountGeneralError {
        return ResponseEntity.ok(accountService.transferMoneyMulti(request));
    }
    @Operation(summary = "Update account", description = "Update account")
    @PutMapping("/{accountId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully", content = @Content(schema = @Schema(implementation = UpdateAccountResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Malformed request"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<UpdateAccountResponseDTO> updateAccount(
            @PathVariable UUID accountId,
            @Validated @RequestBody UpdateAccountRequestDTO request
    ) throws AccountNotFoundException {
        return ResponseEntity.ok(accountService.updateAccount(accountId, request));
    }
    @GetMapping("/{accountId}/transactions")
    @Operation(summary = "Get transaction list", description = "Returns transactions list based on page details and current page info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned list", content = @Content(schema = @Schema(implementation = AccountTransactionListPageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
        }
    )
    public ResponseEntity<AccountTransactionListPageResponseDTO> getAccountTransactions(
            @PathVariable(name = "accountId") UUID accountId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "sort", defaultValue = "DESC", required = false) String sort
    ) throws AccountNotFoundException {

        return ResponseEntity.ok(accountService.getAccountTransactions(accountId, page, size, sort));
    }
}
