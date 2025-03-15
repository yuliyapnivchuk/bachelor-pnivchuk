package com.itstep.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.service.SpeechRecognitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    SpeechRecognitionService speechRecognitionService;

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
}
