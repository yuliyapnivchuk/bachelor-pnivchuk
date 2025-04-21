package com.itstep.mapper;

import com.itstep.dto.EventDto;
import com.itstep.entity.Event;
import com.itstep.exception.EventNotFound;
import com.itstep.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class EventMapperTests {

    @Autowired
    EventMapper eventMapper;

    @MockitoBean
    EventRepository eventRepository;

    @Test
    void mapEventIdToEventEntityWhenEventWithSuchIdNotFoundTest() {
        when(eventRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(EventNotFound.class, () -> eventMapper.mapEventIdToEventEntity(1, eventRepository));
    }

    @Test
    void toEntityTest() {
        EventDto eventDto = new EventDto(1, "name");

        Event entity = eventMapper.toEntity(eventDto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(eventDto.getId());
        assertThat(entity.getName()).isEqualTo(eventDto.getName());
    }

    @Test
    void toEntityWhenEventNullTest() {
        Event entity = eventMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void toDtoTest() {
        Event event = new Event(1, "name", null);

        EventDto eventDto = eventMapper.toDto(event);

        assertThat(eventDto).isNotNull();
        assertThat(eventDto.getId()).isEqualTo(event.getId());
        assertThat(eventDto.getName()).isEqualTo(event.getName());
    }

    @Test
    void toDtoWhenEventNullTest() {
        EventDto eventDto = eventMapper.toDto(null);
        assertThat(eventDto).isNull();
    }
}
