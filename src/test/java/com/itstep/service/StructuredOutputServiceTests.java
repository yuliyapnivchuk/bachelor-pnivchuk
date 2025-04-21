package com.itstep.service;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.ExpenseDto;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.service.impl.StructuredOutputServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
public class StructuredOutputServiceTests {

    @InjectMocks
    private StructuredOutputServiceImpl structuredOutputService;

    @Mock
    private SecretClient secretClient;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    private ObjectMapper objectMapper;

    @Test
    void parseExpenseTest() {
        String jsonResponse = """
                {
                  "choices": [
                    {
                      "message": {
                        "function_call": {
                          "name": "parse_expense",
                          "arguments": "{\\"summary\\": \\"Вхідні квитки в ботанічний сад для двох осіб\\",\\"items\\":[],\\"totalAmount\\":380,\\"currency\\":\\"UAH\\",\\"category\\": \\"ENTERTAINMENT\\"}"
                        }
                      }
                    }
                  ]
                }
                """;
        ResponseEntity<String> response = new ResponseEntity<>(jsonResponse, HttpStatus.OK);

        when(secretClient.getSecret(anyString())).thenReturn(new KeyVaultSecret("name", "value"));
        when(restTemplate.exchange(nullable(String.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(response);

        ExpenseDto result = structuredOutputService.parseExpense("fake text", "user1", 1);

        assertThat(result).isNotNull();
        assertThat(result.getSummary()).isEqualTo("Вхідні квитки в ботанічний сад для двох осіб");
        assertThat(result.getTotalAmount()).isEqualTo(380);
        assertThat(result.getCurrency()).isEqualTo("UAH");
        assertThat(result.getCategory()).isEqualTo("ENTERTAINMENT");
        assertThat(result.getTransactionDate()).isNotNull();
        assertThat(result.getEventId()).isEqualTo(1);
        assertThat(result.getCreatedBy()).isEqualTo("user1");
        assertThat(result.getPayedBy()).isEqualTo("user1");
    }
}
