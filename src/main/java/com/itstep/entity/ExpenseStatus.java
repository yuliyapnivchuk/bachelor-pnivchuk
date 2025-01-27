package com.itstep.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExpenseStatus {
    DRAFT("DRAFT"), SUBMITTED("SUBMITTED");
    public final String status;
}