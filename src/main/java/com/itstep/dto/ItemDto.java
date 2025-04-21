package com.itstep.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class ItemDto {
    private Integer id;
    private String description;
    private Double price;
    private Double quantity;
    private Double totalPrice;
    private String splitType;
    private List<SplitDetailsDto> splitDetails = new ArrayList<>();

    public static ItemDto createItem() {
        return new ItemDto();
    }
}