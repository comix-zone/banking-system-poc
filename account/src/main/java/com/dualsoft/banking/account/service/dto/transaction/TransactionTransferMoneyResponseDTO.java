package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class TransactionTransferMoneyResponseDTO extends BaseDTO {
    private UUID transactionId;
    private UUID correlationId;
    private BigDecimal balance;
    private String currency;
}
