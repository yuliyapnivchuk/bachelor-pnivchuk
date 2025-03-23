package com.itstep.service;

import com.itstep.exception.NonExistingSplitType;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum SplitType {
    EQUAL("="), SHARES("shares"), PERCENTAGE("%"), MANUAL("manual"), BY_ITEM("byItem");
    public final String type;

    public static SplitType get(String type) {
        return Arrays.stream(SplitType.values())
                .filter(item -> item.type.equals(type))
                .findFirst()
                .orElseThrow(() -> new NonExistingSplitType("Non existing split type"));
    }
}