package com.dualsoft.banking.account.service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_owner", schema = "account")
public class AccountOwner {

    @Id
    @Column(name = "ao_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "ao_personal_id")
    private String personalId;

    @Column(name = "ao_name")
    private String name;

    @Column(name = "ao_surname")
    private String surname;

    @Column(name = "ao_email")
    private String email;

    @Column(name = "ao_phone")
    private String phone;

    @Column(name = "ao_address")
    private String address;

    @Column(name = "ao_account_status")
    private int accountStatus;

    @Column(name = "ao_currency")
    private String currency;
}
