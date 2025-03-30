package com.itstep.service;

import com.itstep.dto.ExpenseDto;

import java.util.List;

public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto);
    ExpenseDto getExpense(Integer expenseId);
    ExpenseDto updateExpense(ExpenseDto expenseDto);
    ExpenseDto submitExpense(ExpenseDto expenseDto);
    List<ExpenseDto> getAllExpenses(Integer eventId);
    void delete(Integer id);
}
