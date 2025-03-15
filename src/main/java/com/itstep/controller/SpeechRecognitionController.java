package com.itstep.controller;

import com.itstep.service.SpeechRecognitionService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/speech")
public class SpeechRecognitionController {
    private SpeechRecognitionService speechRecognitionService;

    @PostMapping("/toText")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SneakyThrows
    public Map<String, String> convertSpeechToText(@RequestParam("file") MultipartFile file) {
        File tempFile = File.createTempFile("uploaded-", ".wav");
        file.transferTo(tempFile);

        String text = speechRecognitionService.convertSpeechToText(tempFile.getAbsolutePath());

        tempFile.delete();

        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        return result;
    }
}
