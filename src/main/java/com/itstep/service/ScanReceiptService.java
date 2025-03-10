package com.itstep.service;

import com.itstep.dto.ExpenseDto;

public interface ScanReceiptService {
    ExpenseDto scanReceipt(byte[] image);
}
