package com.itstep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ItemDto;
import com.itstep.dto.SplitDetailsDto;
import com.itstep.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.itstep.TestDataFactory.getExpenseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ExpenseService expenseService;

    @Test
    void createExpenseTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();

        when(expenseService.addExpense(any())).thenReturn(expenseDto);

        MvcResult mvcResult = mockMvc.perform(post("/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExpenseDto responseDto = objectMapper.readValue(jsonResponse, ExpenseDto.class);

        assertThat(mvcResult).isNotNull();
        assertThat(responseDto).isEqualTo(expenseDto);

        verify(expenseService, times(1)).addExpense(any());
    }

    @Test
    void updateExpenseTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();

        when(expenseService.updateExpense(any())).thenReturn(expenseDto);

        MvcResult mvcResult = mockMvc.perform(put("/expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExpenseDto responseDto = objectMapper.readValue(jsonResponse, ExpenseDto.class);

        assertThat(mvcResult).isNotNull();
        assertThat(responseDto).isEqualTo(expenseDto);

        verify(expenseService, times(1)).updateExpense(any());
    }

    @Test
    void getExpenseTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();

        when(expenseService.getExpense(any(Integer.class))).thenReturn(expenseDto);

        MvcResult mvcResult = mockMvc.perform(get("/expense/{id}", 1))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExpenseDto responseDto = objectMapper.readValue(jsonResponse, ExpenseDto.class);

        assertThat(mvcResult).isNotNull();
        assertThat(responseDto).isEqualTo(expenseDto);

        verify(expenseService, times(1)).getExpense(any(Integer.class));
    }

    @Test
    void submitExpenseTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        MvcResult mvcResult = mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isAccepted())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ExpenseDto responseDto = objectMapper.readValue(jsonResponse, ExpenseDto.class);

        assertThat(mvcResult).isNotNull();
        assertThat(responseDto).isEqualTo(expenseDto);

        verify(expenseService, times(1)).submitExpense(any());
    }

    @Test
    void submitExpenseWithMissingMandatoryParamsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setEventId(null);
        expenseDto.setPayedBy(null);
        expenseDto.setTotalAmount(null);
        expenseDto.setSubtotalAmount(null);
        expenseDto.setCurrency(null);
        expenseDto.setCreatedBy(null);
        expenseDto.setSplitType(null);

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Event id is required;",
                        "Payed by is required;",
                        "Total amount is required;",
                        "Subtotal amount is required;",
                        "Currency is required;",
                        "Created by is required;",
                        "Split type is required;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }

    @Test
    void submitExpenseWithMissingItemsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitType("byItem");
        expenseDto.setItems(null);

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Missing Items;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }

    @Test
    void submitExpenseWithInvalidItemSplitTypeDetailsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitType("byItem");

        ItemDto item1 = ItemDto.builder()
                .description("Сік апельсиновий")
                .price(50.0)
                .quantity(0.2)
                .totalPrice(null)
                .splitType("manual")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName("user2").value(null).build(),
                                SplitDetailsDto.builder().userName(null).value(30.0).build()
                        )
                )
                .build();

        ItemDto item2 = ItemDto.builder()
                .description("Американо з молоком")
                .price(65.0)
                .quantity(3.0)
                .totalPrice(195.0)
                .splitType("=")
                .splitDetails(
                        List.of(SplitDetailsDto.builder().userName(null).build())
                )
                .build();

        ItemDto item3 = ItemDto.builder()
                .description("Капучино")
                .price(85.0)
                .quantity(2.0)
                .totalPrice(170.0)
                .splitType("shares")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName(null).value(2.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(null).build()
                        )
                )
                .build();

        ItemDto item4 = ItemDto.builder()
                .description("Реберця")
                .price(210.0)
                .quantity(1.0)
                .totalPrice(210.0)
                .splitType("%")
                .splitDetails(
                        List.of(
                                SplitDetailsDto.builder().userName(null).value(20.0).build(),
                                SplitDetailsDto.builder().userName("user1").value(70.0).build()
                        )
                )
                .build();

        expenseDto.setItems(List.of(item1, item2, item3, item4));

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Missing User for Split Type: manual;",
                        "Missing Value for Split Type: manual;",
                        "Missing item total price;",
                        "Missing User for Split Type: %;",
                        "Percentage sum is not 100% for Split Type: %;",
                        "Missing Value for Split Type: shares;",
                        "Missing User for Split Type: =;",
                        "Missing User for Split Type: shares;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }

    @Test
    void submitExpenseWithInvalidSplitTypeEqualDetailsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitType("=");
        expenseDto.setItems(null);
        expenseDto.setSplitDetails(List.of(SplitDetailsDto.builder().userName(null).build()));

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Missing User for Split Type: =;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }

    @Test
    void submitExpenseWithInvalidSplitTypePercentageDetailsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitType("%");
        expenseDto.setItems(null);
        expenseDto.setSplitDetails(List.of(
                SplitDetailsDto.builder().userName(null).value(20.0).build(),
                SplitDetailsDto.builder().userName("user1").value(70.0).build(),
                SplitDetailsDto.builder().userName("user2").value(null).build()
        ));

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Missing User for Split Type: %;",
                        "Missing Value for Split Type: %;",
                        "Percentage sum is not 100% for Split Type: %;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }

    @Test
    void submitExpenseWithInvalidSplitTypeSharesDetailsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitType("shares");
        expenseDto.setItems(null);
        expenseDto.setSplitDetails(List.of(
                SplitDetailsDto.builder().userName(null).value(2.0).build(),
                SplitDetailsDto.builder().userName("user1").value(null).build()
        ));

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Missing User for Split Type: shares;",
                        "Missing Value for Split Type: shares;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }

    @Test
    void submitExpenseWithInvalidSplitTypeManualDetailsTest() throws Exception {
        ExpenseDto expenseDto = getExpenseDto();
        expenseDto.setSplitType("manual");
        expenseDto.setItems(null);
        expenseDto.setSplitDetails(List.of(
                SplitDetailsDto.builder().userName("user2").value(null).build(),
                SplitDetailsDto.builder().userName(null).value(30.0).build()
        ));

        when(expenseService.submitExpense(any())).thenReturn(expenseDto);

        mockMvc.perform(post("/expense/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", containsInAnyOrder(
                        "Missing User for Split Type: manual;",
                        "Missing Value for Split Type: manual;"
                )));

        verify(expenseService, times(0)).submitExpense(any());
    }
}
