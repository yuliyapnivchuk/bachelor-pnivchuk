package com.itstep.controller;

import com.itstep.service.AnalyseInvoiceService;
import com.itstep.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/receipt")
public class AnalyseInvoiceController {
    private AnalyseInvoiceService service;

    @PostMapping("/analyze")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto analyze(@RequestParam("file") MultipartFile image) {
        return service.analyseInvoice(image.getBytes());
    }
}