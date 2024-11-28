package com.dualsoft.banking.account.service.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseErrorTemplate implements Serializable {
    private String message;
    private int errorCode;
}
