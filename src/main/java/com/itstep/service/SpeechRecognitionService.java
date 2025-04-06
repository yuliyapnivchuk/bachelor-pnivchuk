package com.itstep.service;

import com.itstep.dto.ExpenseDto;
import com.itstep.dto.PromptDto;

public interface SpeechRecognitionService {
    String convertSpeechToText(String audioFilePath);
    ExpenseDto parseExpense(PromptDto userPrompt);
}
