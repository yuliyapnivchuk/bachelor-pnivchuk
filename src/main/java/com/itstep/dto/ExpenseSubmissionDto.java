package com.itstep.dto;

import com.itstep.validation.SplitDetailsConstraint;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@SplitDetailsConstraint
public class ExpenseSubmissionDto {

    private Integer id;

    @NotNull(message = "Event id is required")
    private Integer eventId;

    @NotNull(message = "Payed by is required")
    private String payedBy;

    private String summary;

    private List<ItemDto> items = new ArrayList<>();

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

    @NotNull(message = "Subtotal amount is required")
    private Double subtotalAmount;

    @NotNull(message = "Currency is required")
    private String currency;

    @NotNull(message = "Split type is required")
    private String splitType;

    private List<SplitDetailsDto> splitDetails = new ArrayList<>();

    private LocalDate transactionDate;

    private String transactionTime;

    private String category;

    private String status;

    @NotNull(message = "Created by is required")
    private String createdBy;

    private String image;
}