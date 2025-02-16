package com.itstep.service.impl;

import com.itstep.dto.NoteDto;
import com.itstep.entity.Note;
import com.itstep.mapper.NoteMapper;
import com.itstep.repository.NoteRepository;
import com.itstep.repository.UserRepository;
import com.itstep.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {
    private NoteRepository noteRepository;
    private NoteMapper noteMapper;
    private UserRepository userRepository;

    public NoteDto addNote(NoteDto noteDto) {
        Note note = noteMapper.toEntity(noteDto, userRepository);
        Note savedNote = noteRepository.save(note);
        return noteMapper.toDto(savedNote);
    }
}
