package com.itstep.service.impl;

import com.azure.security.keyvault.secrets.SecretClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.ExpenseDto;
import com.itstep.service.StructuredOutputService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class StructuredOutputServiceImpl implements StructuredOutputService {

    @Value("${openai.structured.output.url}")
    private String structuredOutputURL;

    @Value("${openai.structured.output.model}")
    private String model;

    @Autowired
    private SecretClient secretClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public ExpenseDto parseExpense(String text, String user, Integer eventId) {
        String key = secretClient.getSecret("OPENAI-API-KEY").getValue();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(key);
        headers.setContentType(APPLICATION_JSON);

        String body = """
                {
                  "model": "%s",
                  "messages": [
                    {"role": "user", "content": "%s"}
                  ],
                  "functions": [%s],
                  "function_call": {"name": "parse_expense"}
                }
                """.formatted(model, text, getJsonFunctionString());

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                structuredOutputURL,
                POST,
                entity,
                String.class
        );

        ExpenseDto expense = handleResponse(response);
        expense.setTransactionDate(LocalDate.now());
        expense.setTransactionTime(LocalTime.now().truncatedTo(SECONDS).toString());
        expense.setEventId(eventId);
        expense.setCreatedBy(user);
        expense.setPayedBy(user);
        expense.setSubtotalAmount(expense.getTotalAmount());
        return expense;
    }

    @SneakyThrows
    private String getJsonFunctionString() {
        Path path = Path.of("src/main/resources/json_function_string.json");
        return Files.readString(path);
    }

    @SneakyThrows
    private ExpenseDto handleResponse(ResponseEntity<String> response) {
        String responseBody = response.getBody();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode functionCallNode = rootNode.path("choices").get(0).path("message").path("function_call");
        String argumentsJson = functionCallNode.path("arguments").asText();

        return objectMapper.readValue(argumentsJson, ExpenseDto.class);
    }
}
