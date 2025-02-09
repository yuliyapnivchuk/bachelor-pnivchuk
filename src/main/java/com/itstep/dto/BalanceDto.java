package com.itstep.dto;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BalanceDto {
    private Map<String, Double> totalBalance;
    private Map<String, Double> userOwesTotal;
    private Map<String, Double> userIsOwedTotal;
    private Map<String, Map<String, Double>> userOwes;
    private Map<String, Map<String, Double>> userIsOwed;
}
