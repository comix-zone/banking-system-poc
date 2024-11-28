package com.dualsoft.banking.notification.service;

import com.dualsoft.banking.events.domain.account.AccountEvent;
import com.dualsoft.banking.events.domain.transaction.TransactionEvent;
import com.dualsoft.banking.notification.domain.UserNotification;
import com.dualsoft.banking.notification.domain.UserNotificationDetails;
import com.dualsoft.banking.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SMSService smsService;
    private final EmailService emailService;
    private final NotificationRepository repository;
    public void processAccountCreatedNotification(AccountEvent info) {

        var userDetails = UserNotificationDetails.builder()
                .email(info.getEmail())
                .phone(info.getPhone())
                .id(info.getId())
                .build();

        UserNotification notification = UserNotification.builder()
                .details(userDetails)
                .message(info.toString())
                .build();

        repository.save(userDetails);

        smsService.sendNotification(notification);
        emailService.sendNotification(notification);
    }

    public void processAccountDeletedNotification(AccountEvent info) {
        var accountId = info.getId();

        var userDetails = this.getUserNotificationDetails(accountId);

        UserNotification notification = UserNotification.builder()
                .details(userDetails)
                .message(info.toString())
                .build();

        smsService.sendNotification(notification);
        emailService.sendNotification(notification);

        repository.delete(userDetails);
    }

    public void processAccountUpdatedNotification(AccountEvent info) {
        var userDetails = UserNotificationDetails.builder()
                .id(info.getId())
                .phone(info.getPhone())
                .email(info.getEmail())
                .build();

        UserNotification notification = UserNotification.builder()
                .details(userDetails)
                .message(info.toString())
                .build();

        smsService.sendNotification(notification);
        emailService.sendNotification(notification);

        repository.save(userDetails);
    }

    public void processTransactionExecutedNotification(TransactionEvent info) {

        var accountId = info.getAccountId();

        var userDetails = this.getUserNotificationDetails(accountId);

        UserNotification notification = UserNotification.builder()
                .details(userDetails)
                .message(info.toString())
                .build();

        smsService.sendNotification(notification);
        emailService.sendNotification(notification);

    }
//Todo add caching
    private UserNotificationDetails getUserNotificationDetails(UUID userId) {
        //Todo: add some handling if there is no user notification details
        //      tho it should never happen
        Optional<UserNotificationDetails> details = repository.findById(userId);
        return details.get();
    }
}
