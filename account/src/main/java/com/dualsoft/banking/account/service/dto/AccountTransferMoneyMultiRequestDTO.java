package com.dualsoft.banking.account.service.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountTransferMoneyMultiRequestDTO extends BaseDTO{
    private UUID fromAccountId;
    private UUID correlationId;
    private List<AccountTransferRecipientDTO> recipients;
}