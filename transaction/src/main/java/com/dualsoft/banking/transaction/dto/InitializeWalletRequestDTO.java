package com.dualsoft.banking.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder
public class InitializeWalletRequestDTO {
    private UUID accountId;
    private BigDecimal amount;
    private String currency;
}
