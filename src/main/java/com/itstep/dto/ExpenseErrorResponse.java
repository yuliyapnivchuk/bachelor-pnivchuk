package com.itstep.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseErrorResponse {
    private List<String> errors;
    private LocalDateTime timestamp;
}
