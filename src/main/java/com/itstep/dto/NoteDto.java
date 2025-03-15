package com.itstep.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class NoteDto {
    private Integer id;
    private Integer expenseId;
    private String createdBy;
    private String noteText;
}