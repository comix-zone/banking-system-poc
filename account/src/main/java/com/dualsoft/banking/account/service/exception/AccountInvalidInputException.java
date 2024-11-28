package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountInvalidInputException extends AccountBaseException {
    public AccountInvalidInputException(String message) {
        super(AccountErrors.ACCOUNT_INVALID_INPUT.getCode(), message, HttpStatus.BAD_REQUEST);
    }

    public AccountInvalidInputException() {
        super(AccountErrors.ACCOUNT_INVALID_INPUT.getCode(), AccountErrors.ACCOUNT_INVALID_INPUT.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
