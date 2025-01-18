package com.itstep.controller;

import com.itstep.service.AzureDocIntelligenceService;
import com.itstep.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/receipt")
public class AzureDocIntelligenceController {
    private AzureDocIntelligenceService azureService;

    @PostMapping("/analyze")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public ExpenseDto recognize(@RequestParam("file") MultipartFile image) {
        return azureService.getInfoFromImage(image.getBytes());
    }
}