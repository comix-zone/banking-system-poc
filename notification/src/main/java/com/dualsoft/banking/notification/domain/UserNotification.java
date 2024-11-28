package com.dualsoft.banking.notification.domain;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNotification {
    private UserNotificationDetails details;
    private String message;
}
