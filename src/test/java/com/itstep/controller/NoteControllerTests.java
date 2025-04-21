package com.itstep.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

import static com.itstep.TestDataFactory.getNoteDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void createNoteTest() throws Exception {
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

    @Test
    public void getNotesTest() throws Exception {
        NoteDto note1 = new NoteDto(1, 1, "user1", "some text");
        NoteDto note2 = new NoteDto(2, 1, "user1", "some text");
        List<NoteDto> notes = List.of(note1, note2);

        when(noteService.getAllNotes(any())).thenReturn(notes);

        MvcResult mvcResult = mockMvc.perform(get("/note/{expenseId}", 1))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<NoteDto> response = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertThat(response).isNotNull();
        assertThat(response).containsExactlyInAnyOrderElementsOf(notes);

        verify(noteService, times(1)).getAllNotes(any());
    }

    @Test
    public void deleteTest() throws Exception {
        doNothing().when(noteService).deleteNote(any());

        mockMvc.perform(delete("/note/{id}", 1))
                .andExpect(status().isOk());

        verify(noteService, times(1)).deleteNote(any());
    }
}
