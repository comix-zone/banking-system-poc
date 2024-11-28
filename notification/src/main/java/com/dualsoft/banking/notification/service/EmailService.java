package com.dualsoft.banking.notification.service;

import com.dualsoft.banking.notification.domain.UserNotification;
import com.dualsoft.banking.notification.service.contract.ISendNotification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements ISendNotification {

    @Override
    public void sendNotification(UserNotification notification) {
        log.info("Email notification sent to:" + notification.getDetails().getEmail());
        log.info("Notification content: " + notification.getMessage());
    }
}
