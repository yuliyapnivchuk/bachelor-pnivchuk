package com.itstep.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.EventDto;
import com.itstep.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    EventService eventService;

    @Test
    public void createEventTest() throws Exception {
        EventDto event = new EventDto(1, "test");

        when(eventService.create(any())).thenReturn(event);

        MvcResult mvcResult = mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        EventDto responseDto = objectMapper.readValue(jsonResponse, EventDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto).isEqualTo(event);

        verify(eventService, times(1)).create(any());
    }

    @Test
    public void getAllEventsTest() throws Exception {
        EventDto event1 = new EventDto(1, "test 1");
        EventDto event2 = new EventDto(2, "test 2");
        List<EventDto> events = List.of(event1, event2);

        when(eventService.getAllEvents()).thenReturn(events);

        MvcResult mvcResult = mockMvc.perform(get("/event"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<EventDto> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response).containsExactlyInAnyOrderElementsOf(events);

        verify(eventService, times(1)).getAllEvents();
    }

    @Test
    public void deleteTest() throws Exception {
        doNothing().when(eventService).delete(any());

        mockMvc.perform(delete("/event/{id}", 1))
                .andExpect(status().isOk());

        verify(eventService, times(1)).delete(any());
    }
}
