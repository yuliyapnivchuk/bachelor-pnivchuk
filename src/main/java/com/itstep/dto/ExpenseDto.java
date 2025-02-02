package com.itstep.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ExpenseDto {
    private Integer id;
    private Integer eventId;
    private String payedBy;
    private String summary;
    private List<ItemDto> items = new ArrayList<>();
    private Double totalAmount;
    private Double subtotalAmount;
    private String currency;
    private String splitType;
    private List<SplitDetailsDto> splitDetails = new ArrayList<>();
    private LocalDate transactionDate;
    private String transactionTime;
    private String category;
    private String status;
    private String createdBy;
}