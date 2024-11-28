package com.dualsoft.banking.account.service.dto.transaction;

import com.dualsoft.banking.account.service.dto.BaseDTO;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TransactionTransferMoneyMultiRequestDTO extends BaseDTO {
    private UUID fromAccountId;
    private UUID correlationId;
    private List<TransactionTransferRecipientDTO> recipients;
}
