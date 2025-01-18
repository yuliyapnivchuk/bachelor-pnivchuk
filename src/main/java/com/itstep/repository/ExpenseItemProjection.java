package com.itstep.repository;

public interface ExpenseItemProjection {
    Integer getExpenseId();
    String getPayer();
    Integer getItemId();
    Double getTotalPrice();
    String getSplitType();
    Double getValue();
    String getUserName();
    String getCurrency();
    String toString();
}
