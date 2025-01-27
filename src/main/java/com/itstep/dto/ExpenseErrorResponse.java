package com.itstep.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpenseErrorResponse {
    private List<String> errors;
    private LocalDateTime timestamp;
}
