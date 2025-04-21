package com.itstep.service;

import com.itstep.dto.NoteDto;

import java.util.List;

public interface NoteService {
    NoteDto addNote(NoteDto noteDto);
    List<NoteDto> getAllNotes(Integer expenseId);

    void deleteNote(Integer id);
}
