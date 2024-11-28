package com.dualsoft.banking.transaction.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
@Data
@RequiredArgsConstructor
public class TransactionAdvisoryLockKey {
    private final UUID accountId;

    public long generateHashKey() {
        return accountId.toString().hashCode() & 0xFFFFFFFFL;
    }

}
