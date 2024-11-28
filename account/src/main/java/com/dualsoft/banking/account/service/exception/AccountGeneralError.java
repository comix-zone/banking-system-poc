package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountGeneralError extends AccountBaseException {

    public AccountGeneralError(String message) {
        super(AccountErrors.ACCOUNT_GENERAL_ERROR.getCode(), message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public AccountGeneralError() {
        super(
                AccountErrors.ACCOUNT_GENERAL_ERROR.getCode(),
                AccountErrors.ACCOUNT_GENERAL_ERROR.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
