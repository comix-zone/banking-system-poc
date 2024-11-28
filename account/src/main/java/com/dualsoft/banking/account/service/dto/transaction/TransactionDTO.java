package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import jakarta.persistence.Column;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionDTO extends BaseDTO {
    private UUID transactionId;
    private UUID correlationId;
    private UUID accountId;
    private int transactionType;
    private BigDecimal amount;
    private BigDecimal currentBalance;
    private String currency;
    private Timestamp createdAt;
}
