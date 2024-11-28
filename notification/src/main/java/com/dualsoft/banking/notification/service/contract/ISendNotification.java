package com.dualsoft.banking.notification.service.contract;

import com.dualsoft.banking.events.domain.account.AccountEvent;
import com.dualsoft.banking.notification.domain.UserNotification;

public interface ISendNotification {
    public void sendNotification(UserNotification accountOwner);
}
