package com.itstep.controller;

import com.itstep.service.SpeechRecognitionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/speech")
public class SpeechRecognitionController {
    private SpeechRecognitionService speechRecognitionService;

    @PostMapping("/toText")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> convertSpeechToText(@RequestParam("file") MultipartFile file) {
        File tempFile;

        try {
            tempFile = File.createTempFile("uploaded-", ".wav");
            file.transferTo(tempFile);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to transfer uploaded audio to temp file ", e);
        }

        String text = speechRecognitionService.convertSpeechToText(tempFile.getAbsolutePath());

        tempFile.delete();

        Map<String, String> result = new HashMap<>();
        result.put("text", text);
        return result;
    }
}
