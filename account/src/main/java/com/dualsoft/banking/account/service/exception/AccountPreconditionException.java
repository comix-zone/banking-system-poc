package com.dualsoft.banking.account.service.exception;

import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import org.springframework.http.HttpStatus;

public class AccountPreconditionException extends AccountBaseException {

        public AccountPreconditionException(String message) {
            super(AccountErrors.ACCOUNT_PRECONDITION_FAILED.getCode(), message, HttpStatus.PRECONDITION_FAILED);
        }

        public AccountPreconditionException() {
            super(
                    AccountErrors.ACCOUNT_PRECONDITION_FAILED.getCode(),
                    AccountErrors.ACCOUNT_PRECONDITION_FAILED.getMessage(),
                    HttpStatus.PRECONDITION_FAILED);
        }
}
