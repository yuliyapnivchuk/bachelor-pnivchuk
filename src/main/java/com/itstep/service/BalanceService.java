package com.itstep.service;

import com.itstep.dto.BalanceDto;

public interface BalanceService {
    BalanceDto getBalance(String user);
}
