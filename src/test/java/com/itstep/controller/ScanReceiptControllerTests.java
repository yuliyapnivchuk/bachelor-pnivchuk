package com.itstep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.ExpenseDto;
import com.itstep.service.ScanReceiptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.itstep.TestDataFactory.getExpenseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScanReceiptController.class)
public class ScanReceiptControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ScanReceiptService scanReceiptService;

    @Test
    public void scanReceiptTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        when(scanReceiptService.scanReceipt(any())).thenReturn(expenseDto);

        MvcResult mvcResult = mockMvc.perform(multipart("/receipt/scan")
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExpenseDto responseDto = objectMapper.readValue(jsonResponse, ExpenseDto.class);

        assertThat(mvcResult).isNotNull();
        assertThat(responseDto).isEqualTo(expenseDto);

        verify(scanReceiptService, times(1)).scanReceipt(any());
    }
}
