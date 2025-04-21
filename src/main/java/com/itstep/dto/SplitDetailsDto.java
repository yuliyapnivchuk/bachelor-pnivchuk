package com.itstep.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class SplitDetailsDto {
    private Integer id;
    private String userName;
    private Double value;
}
