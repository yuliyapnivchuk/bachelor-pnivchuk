package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.PromptDto;

public interface StructuredOutputService {
    ExpenseDto parseExpense(PromptDto userPrompt);
}
