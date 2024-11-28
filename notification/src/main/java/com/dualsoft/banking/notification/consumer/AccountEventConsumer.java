package com.dualsoft.banking.notification.consumer;

import com.dualsoft.banking.events.domain.account.AccountEvent;
import com.dualsoft.banking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEventConsumer {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "account-created-topic",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAccountCreatedEvent(AccountEvent event) {
        log.info("Received AccountCreatedEvent: {}", event);
        notificationService.processAccountCreatedNotification(event);
    }

    @KafkaListener(
            topics = "account-deleted-topic",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAccountDeletedEvent(AccountEvent event) {
        log.info("Received AccountDeletedEvent: {}", event);
        notificationService.processAccountDeletedNotification(event);
    }

    @KafkaListener(
            topics = "account-updated-topic",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAccountUpdatedEvent(AccountEvent event) {
        log.info("Received AccountUpdatedEvent: {}", event);
        notificationService.processAccountUpdatedNotification(event);
    }
}
