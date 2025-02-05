package com.itstep.service;

import com.itstep.dto.ExpenseDto;

public interface AnalyseInvoiceService {
    ExpenseDto analyseInvoice(byte[] image);
}
