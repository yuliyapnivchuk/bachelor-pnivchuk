package com.itstep.service;

import com.itstep.dto.NoteDto;
import com.itstep.entity.Note;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.mapper.NoteMapper;
import com.itstep.repository.NoteRepository;
import com.itstep.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static com.itstep.TestDataFactory.getNote;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class NoteServiceTests {

    @Mock
    NoteRepository noteRepository;

    @MockitoSpyBean
    NoteMapper noteMapper;

    @InjectMocks
    NoteServiceImpl noteService;

    @Test
    void addNoteTest() {
        Note note = getNote();
        when(noteMapper.toEntity(any(), any())).thenReturn(note);
        when(noteRepository.save(any())).thenReturn(note);

        NoteDto actualNote = noteService.addNote(noteMapper.toDto(note));

        assertThat(actualNote).isNotNull();
        assertThat(actualNote).isEqualTo(noteMapper.toDto(note));
        verify(noteMapper, times(1)).toEntity(any(), any());
        verify(noteRepository, times(1)).save(any());
    }

    @Test
    void getAllNotesTest() {
        Note note1 = getNote();
        Note note2 = getNote();
        List<Note> notes = List.of(note1, note2);

        when(noteRepository.findByExpenseId(any())).thenReturn(notes);

        List<NoteDto> resultList = noteService.getAllNotes(1);

        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(notes.size());

        verify(noteRepository, times(1)).findByExpenseId(any());
    }

    @Test
    void deleteTest() {
        doNothing().when(noteRepository).deleteById(any());

        noteService.deleteNote(1);

        verify(noteRepository, times(1)).deleteById(any());
    }
}
