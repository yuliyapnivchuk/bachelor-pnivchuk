package com.itstep.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private Integer id;
    private String name;
    private String email;
}