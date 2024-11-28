package com.dualsoft.banking.account.service.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountErrors {
    ACCOUNT_GENERAL_ERROR(1000, "Account General Error"),
    ACCOUNT_NOT_FOUND(1001, "Account Not Found"),
    ACCOUNT_ALREADY_EXISTS(1002, "Account Already Exists"),
    ACCOUNT_SUSPENDED(1003, "Account Suspended"),
    ACCOUNT_BALANCE_INSUFFICIENT(1004, "Account Balance Insufficient"),
    ACCOUNT_INVALID_INPUT(1005, "Account Invalid Input"),
    ACCOUNT_PRECONDITION_FAILED(1006, "Account Precondition Failed"),
    ACCOUNT_REQUEST_VALIDATION_FAILED(1007, "Request Field Validation Failed");


    private final int code;
    private final String message;
}
