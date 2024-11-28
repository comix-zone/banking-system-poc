package com.dualsoft.banking.transaction.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Data
@RequiredArgsConstructor
public class TransactionBaseRuntimeException extends RuntimeException {
    private final int errorCode;
    private final String message;
    private final HttpStatus status;
}
