package com.itstep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDto {
    private Integer id;
    private String description;
    private Double price;
    private Double quantity;
    private Double totalPrice;
    private String splitType;
    private List<String> divideBetween;

    public static ItemDto createItem() {
        return new ItemDto();
    }
}