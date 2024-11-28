package com.dualsoft.banking.account.service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountTransferRecipientDTO extends BaseDTO {
    private UUID toAccountId;
    private BigDecimal amount;
    private String currency;
}
