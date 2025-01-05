package com.itstep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BalanceHeaderDto {
    private Double totalBalance;
    private String currency;
    private Double userOweTotal;
    private Double userIsOwedTotal;
    List<BalanceLineDto> lines = new ArrayList<>();
}
