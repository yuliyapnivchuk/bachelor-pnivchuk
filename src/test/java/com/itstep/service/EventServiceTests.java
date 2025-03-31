package com.itstep.service;

import com.itstep.dto.EventDto;
import com.itstep.entity.Event;
import com.itstep.mapper.EventMapper;
import com.itstep.mapper.MapperTestConfig;
import com.itstep.repository.EventRepository;
import com.itstep.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class EventServiceTests {

    @Mock
    EventRepository eventRepository;

    @MockitoSpyBean
    EventMapper eventMapper;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    public void createTest() {
        Event event = new Event(1, "name 1", null);

        when(eventRepository.save(any())).thenReturn(event);
        when(eventMapper.toEntity(any())).thenReturn(event);

        EventDto result = eventService.create(new EventDto());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(event.getName());

        verify(eventRepository, times(1)).save(any());
        verify(eventMapper, times(1)).toEntity(any());
    }

    @Test
    public void getAllEventsTest() {
        Event event1 = new Event(1, "name 1", null);
        Event event2 = new Event(2, "name 2", null);
        List<Event> events = List.of(event1, event2);

        when(eventRepository.findAll()).thenReturn(events);

        List<EventDto> result = eventService.getAllEvents();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(events.size());
        assertThat(result.getFirst().getId()).isEqualTo(events.getFirst().getId());
        assertThat(result.getFirst().getName()).isEqualTo(events.getFirst().getName());
        assertThat(result.getLast().getId()).isEqualTo(events.getLast().getId());
        assertThat(result.getLast().getName()).isEqualTo(events.getLast().getName());

        verify(eventRepository, times(1)).findAll();
    }

    @Test
    public void delete() {
        doNothing().when(eventRepository).deleteById(any());

        eventService.delete(1);

        verify(eventRepository, times(1)).deleteById(any());
    }
}
