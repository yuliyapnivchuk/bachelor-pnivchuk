package com.itstep.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExpenseStatus {
    DRAFT("DRAFT");
    public final String status;
}