package com.dualsoft.banking.account.service.dto;

import com.dualsoft.banking.account.service.dto.transaction.TransactionDTO;
import lombok.*;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountTransactionListPageResponseDTO extends BaseDTO {
    private List<TransactionDTO> data;
    private int page;
    private int size;
    private long total;
    private String sort;
}
