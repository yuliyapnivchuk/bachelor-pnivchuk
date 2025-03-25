package com.itstep.controller;

import com.itstep.service.ScanReceiptService;
import com.itstep.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/receipt")
public class ScanReceiptController {
    private ScanReceiptService service;

    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExpenseDto scanReceipt(@RequestParam("file") MultipartFile image) {
        try {
            return service.scanReceipt(image.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to read image", e);
        }
    }
}