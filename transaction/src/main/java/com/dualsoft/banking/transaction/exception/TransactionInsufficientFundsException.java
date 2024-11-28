package com.dualsoft.banking.transaction.exception;

import com.dualsoft.banking.transaction.exception.error.TransactionErrors;
import org.springframework.http.HttpStatus;

public class TransactionInsufficientFundsException extends TransactionBaseException {

    public TransactionInsufficientFundsException() {
        super(
                TransactionErrors.TRANSACTION_INSUFFICIENT_BALANCE.getCode(),
                TransactionErrors.TRANSACTION_INSUFFICIENT_BALANCE.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
