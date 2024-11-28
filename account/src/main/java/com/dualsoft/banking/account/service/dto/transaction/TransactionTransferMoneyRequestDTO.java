package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransactionTransferMoneyRequestDTO extends BaseDTO {
    private UUID fromAccountId;
    private UUID toAccountId;
    private UUID correlationId;
    private BigDecimal amount;
    private String currency;
}
