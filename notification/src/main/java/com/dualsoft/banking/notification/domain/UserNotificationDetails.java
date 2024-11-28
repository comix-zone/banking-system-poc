package com.dualsoft.banking.notification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_notification_details", schema = "notification")

public class UserNotificationDetails {
    @Id
    @Column(name = "und_id")
    private UUID id;

    @Column(name = "und_email")
    private String email;

    @Column(name = "und_phone")
    private String phone;
}
