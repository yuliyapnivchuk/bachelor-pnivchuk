package com.itstep.repository;

import com.itstep.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTests {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    EventRepository eventRepository;

    @Test
    void findByIdTest() {
        Event event = new Event();
        event.setName("Event name");
        testEntityManager.persist(event);

        Optional<Event> actualEvent = eventRepository.findById(1);

        assertThat(actualEvent).isPresent();
        assertThat(actualEvent).contains(event);
    }
}
