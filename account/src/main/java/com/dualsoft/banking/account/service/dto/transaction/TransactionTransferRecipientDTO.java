package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionTransferRecipientDTO extends BaseDTO {
    private UUID toAccountId;
    private BigDecimal amount;
    private String currency;
}
