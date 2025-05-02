package com.itstep.service.impl;

import com.azure.security.keyvault.secrets.SecretClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.service.SpeechRecognitionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@Service
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {

    @Value("${openai.speech.to.text.url}")
    private String url;

    @Value("${openai.speech.to.text.model}")
    private String model;
    @Autowired
    private SecretClient secretClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String convertSpeechToText(String audioFilePath) {
        String key = secretClient.getSecret("OPENAI-API-KEY").getValue();

        File file = new File(audioFilePath);
        FileSystemResource fileResource = new FileSystemResource(file);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileResource);
        body.add("model", model);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(key);
        headers.setContentType(MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        JsonNode rootNode = objectMapper.readTree(response.getBody());
        return rootNode.path("text").asText();
    }
}
