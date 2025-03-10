package com.itstep.controller;

import com.itstep.dto.BalanceDto;
import com.itstep.service.BalanceService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/balance")
public class BalanceController {

    private BalanceService balanceService;

    @GetMapping
    @SneakyThrows
    public BalanceDto calculateBalance(@RequestParam String user) {
        return balanceService.getBalance(user);
    }
}