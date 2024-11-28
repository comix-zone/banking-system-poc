package com.dualsoft.banking.transaction.exception.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResponseErrorTemplate implements Serializable {
    private String message;
    private int errorCode;
}
