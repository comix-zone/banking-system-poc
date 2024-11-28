package com.dualsoft.banking.notification.service;

import com.dualsoft.banking.events.domain.account.AccountEvent;
import com.dualsoft.banking.notification.domain.UserNotification;
import com.dualsoft.banking.notification.service.contract.ISendNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSService implements ISendNotification {
    @Override
    public void sendNotification(UserNotification notification) {
        //Todo add logic for sending sms
        log.info("SMS notification sent to :" + notification.getDetails().getPhone());
        log.info("Notification content: " + notification.getMessage());
    }
}
