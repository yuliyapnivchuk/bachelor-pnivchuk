package com.itstep.mapper;

import com.itstep.dto.EventDto;
import com.itstep.dto.ExpenseDto;
import com.itstep.entity.Event;
import com.itstep.entity.Expense;
import com.itstep.exception.EventNotFound;
import com.itstep.repository.EventRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static com.itstep.exception.ConstantsUtility.EVENT_WITH_SUCH_ID_NOT_FOUND;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Named("mapEventEntityToEventId")
    default Integer getId(Event event) {
        return event.getId();
    }

    @Named("mapEventIdToEventEntity")
    @Mapping(target = "expense", ignore = true)
    default Event mapEventIdToEventEntity(Integer eventId, @Context EventRepository eventRepository) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound(EVENT_WITH_SUCH_ID_NOT_FOUND + eventId));
    }

    EventDto toDto(Event event);

    Event toEntity(EventDto eventDto);
}
