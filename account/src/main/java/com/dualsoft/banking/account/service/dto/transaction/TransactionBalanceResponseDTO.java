package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransactionBalanceResponseDTO extends BaseDTO {
    private BigDecimal balance;
    private String currency;
}
