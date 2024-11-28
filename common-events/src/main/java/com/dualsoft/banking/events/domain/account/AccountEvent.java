package com.dualsoft.banking.events.domain.account;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Data
@SuperBuilder
@JsonSerialize
@JsonDeserialize
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID id;
    private String personalId;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private int accountStatus;
    private String currency;
}
