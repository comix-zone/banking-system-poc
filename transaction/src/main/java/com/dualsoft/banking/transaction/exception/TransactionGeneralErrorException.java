package com.dualsoft.banking.transaction.exception;

import com.dualsoft.banking.transaction.exception.error.TransactionErrors;
import org.springframework.http.HttpStatus;

public class TransactionGeneralErrorException extends TransactionBaseException {

    public TransactionGeneralErrorException(String message) {
        super(
                TransactionErrors.TRANSACTION_GENERAL_ERROR.getCode(),
                message,
                HttpStatus.INTERNAL_SERVER_ERROR
                );
    }
}
