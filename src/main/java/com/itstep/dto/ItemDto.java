package com.itstep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDto {
    private Integer id;
    private String description;
    private Double price;
    private Double quantity;
    private Double totalPrice;
    private String assignedTo;

    public static ItemDto createItem() {
        return new ItemDto();
    }
}