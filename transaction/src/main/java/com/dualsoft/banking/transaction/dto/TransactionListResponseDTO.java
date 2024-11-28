package com.dualsoft.banking.transaction.dto;

import com.dualsoft.banking.transaction.domain.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransactionListResponseDTO extends BaseDTO{
    private List<Transaction> data;
    private int page;
    private int size;
    private long total;
    private String sort;
}
