package com.itstep.controller;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ExpenseSubmissionDto;
import com.itstep.dto.NoteDto;
import com.itstep.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    public ExpenseDto createExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.addExpense(expenseDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto updateExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.updateExpense(expenseDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto getExpense(@RequestParam Integer expenseId) {
        return expenseService.getExpense(expenseId);
    }

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto submitExpense(@Valid @RequestBody ExpenseSubmissionDto expenseDto) {
        return expenseService.submitExpense(expenseDto);
    }
}