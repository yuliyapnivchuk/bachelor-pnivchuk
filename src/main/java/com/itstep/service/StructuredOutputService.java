package com.itstep.service;

import com.itstep.dto.ExpenseDto;

public interface StructuredOutputService {
    ExpenseDto parseExpense(String text, String user, Integer eventId);
}
