package com.itstep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itstep.dto.NoteDto;
import com.itstep.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.itstep.TestDataFactory.getNoteDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
public class NoteControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    NoteService noteService;

    @Test
    public void createNote() throws Exception {
        NoteDto note = getNoteDto();

        when(noteService.addNote(any())).thenReturn(note);

        MvcResult mvcResult = mockMvc.perform(post("/note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        NoteDto responseDto = objectMapper.readValue(jsonResponse, NoteDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto).isEqualTo(note);

        verify(noteService, times(1)).addNote(any());
    }
}
