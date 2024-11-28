package com.dualsoft.banking.notification.consumer;

import com.dualsoft.banking.events.domain.account.AccountEvent;
import com.dualsoft.banking.events.domain.transaction.TransactionEvent;
import com.dualsoft.banking.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionEventConsumer {
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "transaction-executed-topic",
            groupId = "notification-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTransactionExecutedEvent(TransactionEvent event) {
        log.info("Received TransactionExecutedEvent: {}", event);
        notificationService.processTransactionExecutedNotification(event);
    }


}
