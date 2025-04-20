package com.itstep.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.ExpenseDto;
import com.itstep.service.SpeechRecognitionService;
import com.itstep.service.StructuredOutputService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpeechRecognitionController.class)
public class SpeechRecognitionControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    @Qualifier("OpenAI")
    private SpeechRecognitionService speechRecognitionService;

    @MockitoBean
    private StructuredOutputService structuredOutputService;

    @Test
    public void convertSpeechToTextTest() throws Exception {
        String text = "Some text that was recognized";
        Map<String, String> expectedResultMap = new HashMap<>();
        expectedResultMap.put("text", text);

        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                "test-audio.wav",
                String.valueOf(MediaType.valueOf("audio/wav")),
                "dummy audio content".getBytes()
        );

        when(speechRecognitionService.convertSpeechToText(any())).thenReturn(text);

        MvcResult mvcResult = mockMvc.perform(multipart("/speech/toText")
                        .file(audioFile)
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        Map<String, String> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertThat(mvcResult).isNotNull();
        assertThat(responseMap).isEqualTo(expectedResultMap);

        verify(speechRecognitionService, times(1)).convertSpeechToText(any());
    }

    @Test
    public void convertSpeechToTextIOExceptionTest() {
        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                "test-audio.wav",
                String.valueOf(MediaType.valueOf("audio/wav")),
                "dummy audio content".getBytes()
        );

        try (MockedStatic<File> mockedFile = mockStatic(File.class)) {
            mockedFile.when(() -> File.createTempFile("uploaded-", ".wav")).thenThrow(new IOException());

            mockMvc.perform(multipart("/speech/toText")
                    .file(audioFile)
                    .contentType(MULTIPART_FORM_DATA)).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void speechToExpenseTest() throws Exception {

        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                "test-audio.wav",
                String.valueOf(MediaType.valueOf("audio/wav")),
                "dummy audio content".getBytes()
        );

        when(speechRecognitionService.convertSpeechToText(any())).thenReturn("Some text that was recognized");
        when(structuredOutputService.parseExpense(any(), any(), any())).thenReturn(new ExpenseDto());

        MvcResult mvcResult = mockMvc.perform(multipart("/speech/toExpense")
                        .file(audioFile)
                        .param("user", "user1")
                        .param("event_id", "1")
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExpenseDto result = objectMapper.readValue(jsonResponse, ExpenseDto.class);

        assertThat(mvcResult).isNotNull();
        assertThat(result).isNotNull();

        verify(speechRecognitionService, times(1)).convertSpeechToText(any());
        verify(structuredOutputService, times(1)).parseExpense(any(), any(), any());
    }

    @Test
    public void speechToExpenseIOExceptionTest() {
        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                "test-audio.wav",
                String.valueOf(MediaType.valueOf("audio/wav")),
                "dummy audio content".getBytes()
        );

        try (MockedStatic<File> mockedFile = mockStatic(File.class)) {
            mockedFile.when(() -> File.createTempFile("uploaded-", ".wav")).thenThrow(new IOException());

            mockMvc.perform(multipart("/speech/toExpense")
                    .file(audioFile)
                    .param("user", "user1")
                    .param("event_id", "1")
                    .contentType(MULTIPART_FORM_DATA)).andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void speechToExpenseExceptionTest() throws Exception {
        MockMultipartFile audioFile = new MockMultipartFile(
                "file",
                "test-audio.wav",
                String.valueOf(MediaType.valueOf("audio/wav")),
                "dummy audio content".getBytes()
        );

        when(structuredOutputService.parseExpense(any(), any(), any())).thenThrow(new RuntimeException());

        mockMvc.perform(multipart("/speech/toExpense")
                .file(audioFile)
                .param("user", "user1")
                .param("event_id", "1")
                .contentType(MULTIPART_FORM_DATA)).andExpect(status().isBadRequest());
    }
}
