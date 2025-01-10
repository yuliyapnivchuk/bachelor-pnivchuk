package com.itstep.controller;

import com.itstep.service.BalanceService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/balance")
public class BalanceController {

    private BalanceService balanceService;

    @GetMapping("/userIsOwed")
    @SneakyThrows
    public Map<String, Map<String, Double>> createExpense(@RequestParam String user) {
        return balanceService.calculateWhatUserIsOwed(user);
    }
}