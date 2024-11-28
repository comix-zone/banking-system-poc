package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountNotFoundRuntimeException extends AccountBaseRuntimeException {
    public AccountNotFoundRuntimeException(String message) {
        super(
                AccountErrors.ACCOUNT_NOT_FOUND.getCode(),
                message,
                HttpStatus.NOT_FOUND
        );
    }

    public AccountNotFoundRuntimeException() {
        super(
                AccountErrors.ACCOUNT_NOT_FOUND.getCode(),
                AccountErrors.ACCOUNT_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }
}
