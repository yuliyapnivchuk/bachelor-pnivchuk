package com.itstep.controller;

import com.itstep.dto.NoteDto;
import com.itstep.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/note")
public class NoteController {

    private NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto createNote(@RequestBody NoteDto noteDto) {
        return noteService.addNote(noteDto);
    }
}
