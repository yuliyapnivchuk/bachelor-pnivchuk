package com.itstep.service;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.models.*;
import com.azure.core.util.polling.SyncPoller;
import com.azure.json.JsonProviders;
import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ItemDto;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.service.impl.AzureDocIntelligenceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class ScanReceiptServiceTests {

    @Mock
    DocumentIntelligenceClient client;

    @Mock
    private SyncPoller<AnalyzeResultOperation, AnalyzeResult> poller;

    @InjectMocks
    AzureDocIntelligenceService azureDocIntelligenceService;

    @Test
    void scanReceiptTest() {
        byte[] image = "test-image".getBytes();
        Path path = Paths.get("src/test/resources/DocumentIntelligenceResponse.json");
        AnalyzeResult mockAnalyzeResult = mockAnalyzeResult(path);

        when(poller.getFinalResult()).thenReturn(mockAnalyzeResult);
        when(client.beginAnalyzeDocument(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(poller);

        ExpenseDto actualExpenseDto = azureDocIntelligenceService.scanReceipt(image);

        assertNotNull(actualExpenseDto);
        assertThat(actualExpenseDto.getTotalAmount()).isEqualTo(2086.0);
        assertThat(actualExpenseDto.getSubtotalAmount()).isEqualTo(2086.0);
        assertThat(actualExpenseDto.getCurrency()).isEqualTo("UAH");
        assertThat(actualExpenseDto.getTransactionTime()).isEqualTo("15:03:00");
        assertThat(actualExpenseDto.getItems().size()).isEqualTo(11);

        ItemDto fistItem = actualExpenseDto.getItems().getFirst();
        ItemDto lastItem = actualExpenseDto.getItems().getLast();
        assertThat(fistItem.getDescription()).isEqualTo("Американо з молоком");
        assertThat(fistItem.getPrice()).isEqualTo(65.0);
        assertThat(fistItem.getQuantity()).isEqualTo(3.0);
        assertThat(fistItem.getTotalPrice()).isEqualTo(195.0);
        assertThat(lastItem.getDescription()).isEqualTo("Супер тертий пляцок з мясом");
        assertThat(lastItem.getPrice()).isEqualTo(185.0);
        assertThat(lastItem.getQuantity()).isEqualTo(1.0);
        assertThat(lastItem.getTotalPrice()).isEqualTo(185.0);

        verify(client, times(1)).beginAnalyzeDocument(anyString(), any(), any(), any(), any(), any(), any(), any(), any());  // Ensure client was called
        verify(poller, times(1)).getFinalResult();
    }

    @Test
    void scanReceiptNegativeScenariosTest() {
        byte[] image = "test-image".getBytes();
        Path path = Paths.get("src/test/resources/DocumentIntelligenceResponseNegativeScenarios.json");
        AnalyzeResult mockAnalyzeResult = mockAnalyzeResult(path);

        when(poller.getFinalResult()).thenReturn(mockAnalyzeResult);
        when(client.beginAnalyzeDocument(anyString(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(poller);

        ExpenseDto actualExpenseDto = azureDocIntelligenceService.scanReceipt(image);

        assertNotNull(actualExpenseDto);
        assertThat(actualExpenseDto.getTotalAmount()).isNull();
        assertThat(actualExpenseDto.getSubtotalAmount()).isNull();
        assertThat(actualExpenseDto.getCurrency()).isNull();
        assertThat(actualExpenseDto.getTransactionTime()).isNull();
        assertTrue(actualExpenseDto.getItems().isEmpty());

        verify(client, times(1)).beginAnalyzeDocument(anyString(), any(), any(), any(), any(), any(), any(), any(), any());  // Ensure client was called
        verify(poller, times(1)).getFinalResult();
    }

    private AnalyzeResult mockAnalyzeResult(Path path) {
        AnalyzeResult analyzeResult = null;
        try {
            String json = new String(Files.readAllBytes(path));
            analyzeResult = AnalyzeResult.fromJson(JsonProviders.createReader(json));

        } catch (IOException e) {
            fail(e.getMessage());
        }
        return analyzeResult;
    }
}
