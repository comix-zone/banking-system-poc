package com.dualsoft.banking.transaction.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferMoneyRequestDTO extends BaseDTO {
    private UUID fromAccountId;
    private UUID toAccountId;
    private UUID correlationId;
    private BigDecimal amount;
    private String currency;
}
