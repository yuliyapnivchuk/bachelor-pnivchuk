package com.itstep.service.impl;

import com.azure.security.keyvault.secrets.SecretClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.ExpenseDto;
import com.itstep.dto.PromptDto;
import com.itstep.exception.SpeechCouldNotBeRecognized;
import com.itstep.service.SpeechRecognitionService;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static com.itstep.exception.ConstantsUtility.SPEECH_COULD_NOT_BE_RECOGNIZED;
import static com.microsoft.cognitiveservices.speech.AutoDetectSourceLanguageConfig.fromLanguages;
import static com.microsoft.cognitiveservices.speech.ResultReason.Canceled;
import static com.microsoft.cognitiveservices.speech.ResultReason.RecognizedSpeech;
import static com.microsoft.cognitiveservices.speech.CancellationReason.Error;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {

    @Autowired
    private SpeechConfig speechConfig;

    @Value("${azure.speech.supported.languages}")
    private String languages;

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

    @SneakyThrows
    public String convertSpeechToText(String audioFilePath) {

        AutoDetectSourceLanguageConfig autoDetectSourceLanguageConfig = fromLanguages(getSupportedLanguages());

        AudioConfig audioConfig = AudioConfig.fromWavFileInput(audioFilePath);
        SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, autoDetectSourceLanguageConfig, audioConfig);

        Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
        SpeechRecognitionResult speechRecognitionResult = task.get();

        if (speechRecognitionResult.getReason() == RecognizedSpeech) {
            return speechRecognitionResult.getText();
        } else if (speechRecognitionResult.getReason() == Canceled) {
            CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
            String errorMsg = SPEECH_COULD_NOT_BE_RECOGNIZED + cancellation.getReason();

            if (cancellation.getReason() == Error) {
                errorMsg = errorMsg + cancellation.getErrorCode() + cancellation.getErrorDetails();
            }

            throw new SpeechCouldNotBeRecognized(errorMsg);
        } else {
            throw new SpeechCouldNotBeRecognized(SPEECH_COULD_NOT_BE_RECOGNIZED);
        }
    }

    public ExpenseDto parseExpense(PromptDto userPrompt) {
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
                """.formatted(model, userPrompt.getPrompt(), getJsonFunctionString());

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
        expense.setEventId(userPrompt.getEventId());
        expense.setCreatedBy(userPrompt.getUser());
        expense.setPayedBy(userPrompt.getUser());
        expense.setSubtotalAmount(expense.getTotalAmount());
        return expense;
    }

    private List<String> getSupportedLanguages() {
        return Arrays.asList(languages.split(","));
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
