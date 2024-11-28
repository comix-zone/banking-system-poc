package com.dualsoft.banking.account.service.dto;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateAccountRequestDTO extends CreateAccountRequestDTO {
}
