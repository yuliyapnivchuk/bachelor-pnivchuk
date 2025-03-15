package com.itstep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.BalanceDto;
import com.itstep.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.itstep.TestDataFactory.getBalanceDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceController.class)
public class BalanceControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BalanceService balanceService;

    @Test
    void calculateBalanceTest() throws Exception {
        BalanceDto balanceDto = getBalanceDto();

        when(balanceService.getBalance(any())).thenReturn(balanceDto);

        MvcResult mvcResult = mockMvc.perform(get("/balance").param("user", "user1"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        BalanceDto responseBalanceDto = objectMapper.readValue(jsonResponse, BalanceDto.class);

        assertThat(responseBalanceDto).isNotNull();
        assertThat(responseBalanceDto).isEqualTo(balanceDto);

        verify(balanceService, times(1)).getBalance(any());
    }
}
