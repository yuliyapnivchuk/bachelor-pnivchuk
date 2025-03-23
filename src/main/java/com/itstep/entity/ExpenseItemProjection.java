package com.itstep.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ExpenseItemProjection {
    private Integer expenseId;
    private String payer;
    private Integer itemId;
    private Double totalPrice;
    private String splitType;
    private Double value;
    private String userName;
    private String currency;
}
