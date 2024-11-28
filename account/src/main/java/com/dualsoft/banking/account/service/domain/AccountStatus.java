package com.dualsoft.banking.account.service.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatus {
    PENDING_ACTIVATION(0),
    ACTIVE(1),
    PENDING_DELETION(2),
    DELETED(3),
    BLOCKED(4);

    private final int statusCode;
}
