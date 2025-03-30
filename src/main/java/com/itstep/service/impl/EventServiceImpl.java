package com.itstep.service.impl;

import com.itstep.dto.EventDto;
import com.itstep.entity.Event;
import com.itstep.mapper.EventMapper;
import com.itstep.repository.EventRepository;
import com.itstep.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private EventMapper eventMapper;

    public EventDto create(EventDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toDto(savedEvent);
    }

    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(i -> eventMapper.toDto(i)).toList();
    }

    public void delete(Integer id) {
        eventRepository.deleteById(id);
    }
}
