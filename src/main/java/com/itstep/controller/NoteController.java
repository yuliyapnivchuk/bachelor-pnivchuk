package com.itstep.controller;

import com.itstep.dto.NoteDto;
import com.itstep.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("{expenseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<NoteDto> getNotes(@PathVariable Integer expenseId) {
        return noteService.getAllNotes(expenseId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNote(@PathVariable Integer id) {
        noteService.deleteNote(id);
    }
}
