package com.itstep.controller;

import com.itstep.service.ScanReceiptService;
import com.itstep.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/receipt")
public class ScanReceiptController {
    private ScanReceiptService service;

    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto scanReceipt(@RequestParam("file") MultipartFile image) {
        return service.scanReceipt(image.getBytes());
    }
}