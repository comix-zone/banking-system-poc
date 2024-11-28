package com.dualsoft.banking.notification.repository;

import com.dualsoft.banking.notification.domain.UserNotificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<UserNotificationDetails, UUID> {
}
