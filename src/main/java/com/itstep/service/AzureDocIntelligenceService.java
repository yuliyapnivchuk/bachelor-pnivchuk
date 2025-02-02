package com.itstep.service;

import com.itstep.dto.ExpenseDto;

public interface AzureDocIntelligenceService {
    ExpenseDto getInfoFromImage(byte[] image);
}
