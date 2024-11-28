package com.dualsoft.banking.transaction.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferRecipientDTO extends BaseDTO{
    private UUID toAccountId;
    private BigDecimal amount;
    private String currency;
}
