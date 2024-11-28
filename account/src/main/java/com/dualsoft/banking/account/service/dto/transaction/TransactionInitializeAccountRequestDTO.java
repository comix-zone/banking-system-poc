package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;



@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransactionInitializeAccountRequestDTO extends BaseDTO {
    private UUID accountId;
    private BigDecimal amount;
    private String currency;
}
