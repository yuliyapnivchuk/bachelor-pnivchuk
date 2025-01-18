package com.itstep.mapper;

import com.itstep.dto.ExpenseDto;
import com.itstep.entity.Event;
import com.itstep.entity.Expense;
import com.itstep.exception.EventNotFound;
import com.itstep.repository.EventRepository;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static com.itstep.exception.ConstantsUtility.EVENT_WITH_SUCH_ID_NOT_FOUND;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, SplitExpenseMapper.class, UserMapper.class})
public interface ExpenseMapper {
    @Mapping(source = "payedBy", target = "payer", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "eventId", target = "event", qualifiedByName = "mapEventIdToEventEntity")
    Expense toEntity(ExpenseDto expenseDto, @Context EventRepository eventRepository,
                     @Context UserRepository userRepository);


    @Named("mapEventIdToEventEntity")
    default Event toEntity(Integer eventId, @Context EventRepository eventRepository) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound(EVENT_WITH_SUCH_ID_NOT_FOUND + eventId));
    }

    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "payer", target = "payedBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "event", target = "eventId", qualifiedByName = "mapEventEntityToEventDto")
    ExpenseDto toDto(Expense expense);

    @Named("mapEventEntityToEventDto")
    default Integer toDto(Event event) {
        return event.getId();
    }
}