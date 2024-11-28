package com.dualsoft.banking.transaction.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionErrors {
    TRANSACTION_GENERAL_ERROR(2000, "Transaction Service General Error"),
    TRANSACTION_NOT_FOUND(2001, "Transaction not found"),
    TRANSACTION_ALREADY_EXISTS(2002, "Transaction already exists"),
    TRANSACTION_INSUFFICIENT_BALANCE(2003, "Insufficient balance"),
    TRANSACTION_ALREADY_INITIALIZED(2004, "Account already initialized");

    private final int code;
    private final String message;
}
