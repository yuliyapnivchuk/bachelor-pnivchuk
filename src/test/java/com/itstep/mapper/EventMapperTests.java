package com.itstep.mapper;

import com.itstep.exception.EventNotFound;
import com.itstep.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class EventMapperTests {

    @Autowired
    EventMapper eventMapper;

    @MockitoBean
    EventRepository eventRepository;

    @Test
    void toEntityEventWithSuchIdNotFoundTest() {
        when(eventRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(EventNotFound.class, () -> eventMapper.toEntity(1, eventRepository));
    }
}
