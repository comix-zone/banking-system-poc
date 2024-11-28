package com.dualsoft.banking.transaction.exception;

import com.dualsoft.banking.transaction.exception.error.TransactionErrors;
import org.springframework.http.HttpStatus;

public class TransactionNotFoundRuntimeException extends TransactionBaseRuntimeException {
    public TransactionNotFoundRuntimeException() {
        super(
                TransactionErrors.TRANSACTION_NOT_FOUND.getCode(),
                TransactionErrors.TRANSACTION_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
}
