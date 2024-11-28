package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountGeneralRuntimeException extends AccountBaseRuntimeException {
    public AccountGeneralRuntimeException() {
        super(
                AccountErrors.ACCOUNT_GENERAL_ERROR.getCode(),
                AccountErrors.ACCOUNT_GENERAL_ERROR.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
