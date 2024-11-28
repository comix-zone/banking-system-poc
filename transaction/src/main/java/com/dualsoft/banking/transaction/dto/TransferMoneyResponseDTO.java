package com.dualsoft.banking.transaction.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransferMoneyResponseDTO extends BaseDTO {
    private UUID transactionId;
    private UUID correlationId;
    private BigDecimal balance;
    private String currency;
}
