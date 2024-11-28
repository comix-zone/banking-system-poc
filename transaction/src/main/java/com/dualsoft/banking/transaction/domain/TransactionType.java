package com.dualsoft.banking.transaction.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    DEPOSIT(1, "DEPOSIT"),
    WITHDRAWAL(2, "WITHDRAW"),
    CANCEL_DEPOSIT(3, "CANCEL_DEPOSIT"),
    CANCEL_WITHDRAWAL(4, "CANCEL_WITHDRAW");

    private final int value;
    private final String name;

    public static String getNameByValue(int value) {
        for (TransactionType type : TransactionType.values()) {
            if (type.value == value) {
                return type.name;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
