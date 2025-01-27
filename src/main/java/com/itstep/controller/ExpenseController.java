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

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    public ExpenseDto createExpense(@RequestBody ExpenseDto expenseDto) {
        return expenseService.addExpense(expenseDto);
    }

    @PostMapping("/note")
    @ResponseStatus(HttpStatus.CREATED)
    @SneakyThrows
    public NoteDto createNote(@RequestBody NoteDto noteDto) {
        return expenseService.addNote(noteDto);
    }

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto updateExpense(@Valid @RequestBody ExpenseSubmissionDto expenseDto) {
        return expenseService.submitExpense(expenseDto);
    }
}