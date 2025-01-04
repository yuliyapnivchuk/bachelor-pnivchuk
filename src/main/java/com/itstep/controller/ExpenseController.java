package com.itstep.controller;

import com.itstep.dto.ExpenseDto;
import com.itstep.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    public ExpenseDto createExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.addExpense(expenseDto);
    }
}