package com.dualsoft.banking.transaction.advice;

import com.dualsoft.banking.transaction.exception.TransactionBaseException;
import com.dualsoft.banking.transaction.exception.TransactionGeneralErrorException;
import com.dualsoft.banking.transaction.exception.TransactionInsufficientFundsException;
import com.dualsoft.banking.transaction.exception.TransactionUserNotFoundException;
import com.dualsoft.banking.transaction.exception.error.ResponseErrorTemplate;
import com.dualsoft.banking.transaction.exception.error.TransactionErrors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.dualsoft.banking.transaction")
public class ErrorHandler {

    @ExceptionHandler({
            TransactionGeneralErrorException.class,
            TransactionUserNotFoundException.class,
            TransactionInsufficientFundsException.class})
    public ResponseEntity<ResponseErrorTemplate> handleTransactionServiceExceptions(TransactionBaseException e) {
        log.error("Transaction exception: " + e.getMessage());
        ResponseErrorTemplate errorTemplate = new ResponseErrorTemplate(e.getMessage(), e.getErrorCode());
        return new ResponseEntity<>(errorTemplate, e.getStatus());
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<ResponseErrorTemplate> handleException(Exception e) {
        log.error("System error :" + e.getMessage());
        ResponseErrorTemplate errorTemplate = new ResponseErrorTemplate(
                TransactionErrors.TRANSACTION_GENERAL_ERROR.getMessage(),
                TransactionErrors.TRANSACTION_GENERAL_ERROR.getCode()
                );
        return new ResponseEntity<>(errorTemplate, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            UnexpectedRollbackException.class
    })
    public ResponseEntity<ResponseErrorTemplate> handleRollbackException(Exception e) {
        log.error("Transaction exception :" + e.getMessage());
        ResponseErrorTemplate errorTemplate = new ResponseErrorTemplate(
                TransactionErrors.TRANSACTION_INSUFFICIENT_BALANCE.getMessage(),
                TransactionErrors.TRANSACTION_INSUFFICIENT_BALANCE.getCode()
        );
        return new ResponseEntity<>(errorTemplate, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
