package com.itstep.mapper;

import com.itstep.entity.Event;
import com.itstep.exception.EventNotFound;
import com.itstep.repository.EventRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import static com.itstep.exception.ConstantsUtility.EVENT_WITH_SUCH_ID_NOT_FOUND;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Named("mapEventEntityToEventDto")
    default Integer toDto(Event event) {
        return event.getId();
    }

    @Named("mapEventIdToEventEntity")
    default Event toEntity(Integer eventId, @Context EventRepository eventRepository) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound(EVENT_WITH_SUCH_ID_NOT_FOUND + eventId));
    }
}
