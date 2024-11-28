package com.dualsoft.banking.transaction.exception;

import com.dualsoft.banking.transaction.exception.error.TransactionErrors;
import org.springframework.http.HttpStatus;

public class TransactionDoubleInitializationException extends TransactionBaseException {
    public TransactionDoubleInitializationException() {
        super(
                TransactionErrors.TRANSACTION_ALREADY_INITIALIZED.getCode(),
                TransactionErrors.TRANSACTION_ALREADY_INITIALIZED.getMessage(),
                HttpStatus.CONFLICT
        );
    }
}
