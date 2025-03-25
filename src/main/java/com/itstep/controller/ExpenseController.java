package com.itstep.controller;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ExpenseSubmissionDto;
import com.itstep.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseDto createExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.addExpense(expenseDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseDto updateExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.updateExpense(expenseDto);
    }

    @GetMapping("{expenseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseDto getExpense(@PathVariable Integer expenseId) {
        return expenseService.getExpense(expenseId);
    }

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseDto submitExpense(@Valid @RequestBody ExpenseSubmissionDto expenseDto) {
        return expenseService.submitExpense(expenseDto);
    }
}