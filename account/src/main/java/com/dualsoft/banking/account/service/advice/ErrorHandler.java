package com.dualsoft.banking.account.service.advice;

import com.dualsoft.banking.account.service.exception.*;
import com.dualsoft.banking.account.service.exception.error.AccountErrors;
import com.dualsoft.banking.account.service.exception.error.ResponseErrorTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.dualsoft.banking.account.service")
public class ErrorHandler {
    @ExceptionHandler({
            AccountAlreadyExistsException.class,
            AccountPreconditionException.class,
            AccountAlreadyExistsException.class,
            AccountInvalidInputException.class,
            AccountNotFoundException.class,
            AccountGeneralError.class
    })
    public ResponseEntity<ResponseErrorTemplate> handleAccountBaseException(AccountBaseException e) {
        log.error("AccountBaseException: ", e);
        ResponseErrorTemplate responseErrorTemplate = new ResponseErrorTemplate(e.getMessage(), e.getErrorCode());
        log.error("Returning status: {}", e.getStatus());
        return new ResponseEntity<>(responseErrorTemplate, e.getStatus());
    }
    @ExceptionHandler({
            AccountGeneralRuntimeException.class,
            AccountNotFoundRuntimeException.class,
            AccountPreconditionRuntimeException.class,
            AccountBalanceInsufficientRuntimeException.class,
            AccountAlreadyExistsRuntimeException.class
    })
    public ResponseEntity<ResponseErrorTemplate> handleAccountBaseRuntimeException(AccountBaseRuntimeException e) {
        log.error("AccountBaseRuntimeException: ", e);
        ResponseErrorTemplate responseErrorTemplate = new ResponseErrorTemplate(e.getMessage(), e.getErrorCode());
        log.error("Returning status: {}", e.getStatus());
        return new ResponseEntity<>(responseErrorTemplate, e.getStatus());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseErrorTemplate> handleRuntimeException(RuntimeException e) {
        log.info("RuntimeException: ", e);
        ResponseErrorTemplate responseErrorTemplate = new ResponseErrorTemplate(
                    AccountErrors.ACCOUNT_GENERAL_ERROR.getMessage(),
                    AccountErrors.ACCOUNT_GENERAL_ERROR.getCode()
            );

        return new ResponseEntity<>(responseErrorTemplate, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseErrorTemplate> handleException(Exception e) {
        log.error("Exception: ", e);
        ResponseErrorTemplate responseErrorTemplate = new ResponseErrorTemplate(
                AccountErrors.ACCOUNT_GENERAL_ERROR.getMessage(),
                AccountErrors.ACCOUNT_GENERAL_ERROR.getCode());
        return new ResponseEntity<>(responseErrorTemplate, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorTemplate> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "Request validation failed: ";
        errorMessage += ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.error(errorMessage);
        ResponseErrorTemplate responseErrorTemplate = new ResponseErrorTemplate(errorMessage, AccountErrors.ACCOUNT_REQUEST_VALIDATION_FAILED.getCode());
        return new ResponseEntity<>(responseErrorTemplate, HttpStatus.BAD_REQUEST);
    }
}
