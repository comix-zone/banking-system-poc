package com.dualsoft.banking.account.service.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferMoneyResponseDTO extends BaseDTO {
    private UUID transactionId;
    private UUID correlationId;
    private BigDecimal balance;
    private String currency;
}
