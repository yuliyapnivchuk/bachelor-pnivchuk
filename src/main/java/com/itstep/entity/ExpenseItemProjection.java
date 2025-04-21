package com.itstep.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExpenseItemProjection {
    private Integer expenseId;
    private String payer;
    private Integer itemId;
    private Double totalPrice;
    private String splitType;
    private Double value;
    private String userName;
    private String currency;

    public ExpenseItemProjection(Integer expenseId, Integer itemId, BigDecimal totalPrice, String currency, String splitType, BigDecimal value, String userName) {
        this.expenseId = expenseId;
        this.itemId = itemId;
        this.totalPrice = (totalPrice != null) ? totalPrice.doubleValue() : null;
        this.currency = currency;
        this.splitType = splitType;
        this.value = (value != null) ? value.doubleValue() : null;
        this.userName = userName;
    }

    public ExpenseItemProjection(Integer expenseId, String payer, Integer itemId, BigDecimal totalPrice, String currency, String splitType, BigDecimal value, String userName) {
        this.expenseId = expenseId;
        this.payer = payer;
        this.itemId = itemId;
        this.totalPrice = (totalPrice != null) ? totalPrice.doubleValue() : null;
        this.splitType = splitType;
        this.value = (value != null) ? value.doubleValue() : null;
        this.userName = userName;
        this.currency = currency;
    }
}
