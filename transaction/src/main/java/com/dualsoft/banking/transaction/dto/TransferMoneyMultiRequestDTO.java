package com.dualsoft.banking.transaction.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransferMoneyMultiRequestDTO extends BaseDTO{
    private UUID fromAccountId;
    private UUID correlationId;
    private List<TransferRecipientDTO> recipients;
}
