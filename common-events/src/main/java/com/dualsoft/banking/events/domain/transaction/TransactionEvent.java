package com.dualsoft.banking.events.domain.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;
@Data
@SuperBuilder
@JsonSerialize
@JsonDeserialize
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID accountId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID localTransactionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID correlationTransactionId;
    private String type;
    private BigDecimal amount;
    private BigDecimal balance;
    private Timestamp executedAt;
}
