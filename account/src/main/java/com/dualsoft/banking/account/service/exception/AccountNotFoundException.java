package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AccountBaseException {
    public AccountNotFoundException(String message) {
        super(AccountErrors.ACCOUNT_NOT_FOUND.getCode(), message, HttpStatus.NOT_FOUND);
    }

    public AccountNotFoundException() {
        super(AccountErrors.ACCOUNT_NOT_FOUND.getCode(), AccountErrors.ACCOUNT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
    }
}
