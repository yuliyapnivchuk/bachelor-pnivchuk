package com.itstep.service;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum SplitType {
    EQUAL("="), SHARES("shares"), PERCENTAGE("%"), MANUAL("manual");
    public final String type;

    public static SplitType get(String type) {
        return Arrays.stream(SplitType.values())
                .filter(item -> item.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}