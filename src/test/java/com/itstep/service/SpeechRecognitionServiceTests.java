package com.itstep.service;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.service.impl.SpeechRecognitionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class SpeechRecognitionServiceTests {

    @Mock
    private SecretClient secretClient;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    private ObjectMapper objectMapper;

    @InjectMocks
    private SpeechRecognitionServiceImpl speechRecognitionService;

    @Test
    void convertSpeechToTextTest() {
        String jsonResponse = "{\"text\":\"fake text\"}";
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(secretClient.getSecret(anyString())).thenReturn(new KeyVaultSecret("name", "value"));
        when(restTemplate.postForEntity(nullable(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(response);

        String result = speechRecognitionService.convertSpeechToText("");
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("fake text");

    }
}
