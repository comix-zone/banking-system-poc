package com.dualsoft.banking.account.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UpdateAccountResponseDTO extends BaseDTO {
    private UUID id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String address;

    private int accountStatus;

    private String currency;
}