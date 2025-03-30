package com.itstep.service;

import com.itstep.dto.EventDto;

import java.util.List;

public interface EventService {
    EventDto create(EventDto eventDto);
    List<EventDto> getAllEvents();
    void delete(Integer id);
}
