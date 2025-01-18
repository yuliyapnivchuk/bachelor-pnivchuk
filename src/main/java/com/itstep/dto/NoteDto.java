package com.itstep.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoteDto {
    private Integer id;
    private Integer expenseId;
    private String createdBy;
    private String noteText;
}