package com.dualsoft.banking.account.service.exception;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Data
@RequiredArgsConstructor
public class AccountBaseException extends Exception {
    private final int errorCode;
    private final String message;
    private final HttpStatus status;
}
