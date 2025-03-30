package com.itstep.controller;

import com.itstep.dto.EventDto;
import com.itstep.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {

    private EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody EventDto eventDto) {
        return eventService.create(eventDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id) {
        eventService.delete(id);
    }
}
