package com.dualsoft.banking.account.service.exception;


import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountAlreadyExistsRuntimeException extends AccountBaseRuntimeException {
    public AccountAlreadyExistsRuntimeException(String message) {
        super(AccountErrors.ACCOUNT_ALREADY_EXISTS.getCode(), message, HttpStatus.CONFLICT);
    }
    public AccountAlreadyExistsRuntimeException() {
        super(AccountErrors.ACCOUNT_ALREADY_EXISTS.getCode(), AccountErrors.ACCOUNT_ALREADY_EXISTS.getMessage(), HttpStatus.CONFLICT);
    }
}
