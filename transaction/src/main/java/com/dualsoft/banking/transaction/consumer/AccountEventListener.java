//package com.dualsoft.banking.transaction.consumer;
//
//import com.dualsoft.banking.events.domain.account.AccountEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class AccountEventListener {
//    @KafkaListener(
//            topics = "account-created-topic",
//            groupId = "transaction-service-group",
//            containerFactory = "kafkaListenerContainerFactory"
//    )
//    public void consumeAccountCreatedEvent(AccountEvent event) {
//        log.info("Received AccountCreatedEvent: {}", event);
//        //notificationService.processAccountCreatedNotification(event);
//    }
//}
