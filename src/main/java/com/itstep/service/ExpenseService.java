package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ExpenseSubmissionDto;

public interface ExpenseService {
    ExpenseDto addExpense(ExpenseDto expenseDto);
    ExpenseDto getExpense(Integer expenseId);
    ExpenseDto updateExpense(ExpenseDto expenseDto);
    ExpenseDto submitExpense(ExpenseSubmissionDto expenseSubmissionDto);
}
