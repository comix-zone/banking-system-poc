package com.dualsoft.banking.account.service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateAccountRequestDTO extends BaseDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @NotBlank(message = "Surname cannot be blank")
    @Size(max = 50, message = "Surname must be at most 50 characters")
    private String surname;

    @NotBlank(message = "Personal ID cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9]{6,20}$", message = "Personal ID must be alphanumeric and 6-20 characters long")
    private String personalId;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone must be a valid number with 7-15 digits, optionally starting with '+'")
    private String phone;

    @PositiveOrZero(message = "Deposit amount cannot be negative")
    private BigDecimal depositAmount;

    @NotBlank(message = "Currency cannot be blank")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid ISO 4217 currency code (e.g., USD, EUR)")
    private String currency;
}

