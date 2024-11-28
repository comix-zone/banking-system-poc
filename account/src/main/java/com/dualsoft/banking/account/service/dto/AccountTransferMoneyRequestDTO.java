package com.dualsoft.banking.account.service.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class AccountTransferMoneyRequestDTO extends BaseDTO {
    private UUID fromAccountId;
    private UUID toAccountId;
    private UUID correlationId;
    private BigDecimal amount;
    private String currency;
}
