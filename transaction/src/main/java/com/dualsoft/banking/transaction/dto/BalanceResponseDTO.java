package com.dualsoft.banking.transaction.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class BalanceResponseDTO extends BaseDTO {
    private BigDecimal balance;
    private String currency;
}
