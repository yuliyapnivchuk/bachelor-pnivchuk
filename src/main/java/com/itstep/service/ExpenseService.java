package com.itstep.service;

import com.itstep.dto.ExpenseDto;

public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto);
    ExpenseDto getExpense(Integer expenseId);
    ExpenseDto updateExpense(ExpenseDto expenseDto);
    ExpenseDto submitExpense(ExpenseDto expenseDto);
}
