package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountBalanceInsufficientException extends AccountBaseException {
    public AccountBalanceInsufficientException () {
        super(
                AccountErrors.ACCOUNT_BALANCE_INSUFFICIENT.getCode(),
                AccountErrors.ACCOUNT_BALANCE_INSUFFICIENT.getMessage(),
                HttpStatus.PRECONDITION_FAILED);
    }

}