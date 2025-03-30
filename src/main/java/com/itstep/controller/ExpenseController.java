package com.itstep.controller;

import com.itstep.dto.ExpenseDto;
import com.itstep.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ExpenseDto> getAllExpenses(@PathParam("eventId") Integer eventId) {
        return expenseService.getAllExpenses(eventId);
    }

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseDto submitExpense(@Valid @RequestBody ExpenseDto expenseDto) {
        return expenseService.submitExpense(expenseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        expenseService.delete(id);
    }
}