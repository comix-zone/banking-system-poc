package com.dualsoft.banking.transaction.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction", schema = "transaction")
public class Transaction {
    //@TODO: create ID generator or service to generate unique IDs
    //       IDs are given in batches to clients
    // Generally maybe its better to be number
    @Id
    @Column(name = "t_id")
    private UUID transactionId;

    // Id that can identify a group of transactions. I.e. a deposit and a withdrawal
    // used for a purpose of canceling a group of transactions
    @Column(name = "t_correlation_id")
    private UUID correlationId;

    @Column(name = "t_account_id")
    private UUID accountId;

    @Column(name = "t_transaction_type")
    private int transactionType;

    // TODO: Amount can be treated as string (255) to support different currencies, crypto, etc.
    //       and then converted back to BigDecimal when fetching from DB
    @Column(name = "t_amount")
    private BigDecimal amount;

    @Column(name = "t_current_balance")
    private BigDecimal currentBalance;

    @Column(name = "t_currency")
    private String currency;

    @CreationTimestamp
    @Column(name = "t_created_at")
    private Timestamp createdAt;
}
