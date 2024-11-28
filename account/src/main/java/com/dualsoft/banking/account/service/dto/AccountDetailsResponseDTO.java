package com.dualsoft.banking.account.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class AccountDetailsResponseDTO extends BaseDTO{
    private UUID id;

    private String name;

    private String surname;

    private String personalId;

    private String email;

    private String phone;

    private String address;

    private int accountStatus;

    private String currency;

    private BigDecimal balance;
}
