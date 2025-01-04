package com.itstep.mapper;

import com.itstep.dto.ExpenseDto;
import com.itstep.entity.Event;
import com.itstep.entity.Expense;
import com.itstep.entity.User;
import com.itstep.exception.EventNotFound;
import com.itstep.exception.UserNotFound;
import com.itstep.repository.EventRepository;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

import static com.itstep.exception.ConstantsUtility.EVENT_WITH_SUCH_ID_NOT_FOUND;
import static com.itstep.exception.ConstantsUtility.USER_WITH_SUCH_NAME_NOT_FOUND;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface ExpenseMapper {
    @Mapping(source = "divideBetween", target = "divideBetween", qualifiedByName = "mapUserNameListToUserEntityList")
    @Mapping(source = "payedBy", target = "payer", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "eventId", target = "event", qualifiedByName = "mapEventIdToEventEntity")
    Expense mapExpenseDtoToExpenseEntity(ExpenseDto expenseDto, @Context EventRepository eventRepository,
                                         @Context UserRepository userRepository);


    @Named("mapEventIdToEventEntity")
    default Event mapEventIdToEventEntity(Integer eventId, @Context EventRepository eventRepository) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound(EVENT_WITH_SUCH_ID_NOT_FOUND + eventId));
    }

    @Named("mapUserNameListToUserEntityList")
    default List<User> mapDivideBetweenToUserEtityList(List<String> divideBetweenList,
                                                       @Context UserRepository userRepository) {
        if (divideBetweenList == null) {
            return null;
        }

        return divideBetweenList
                .stream()
                .map(userName -> userRepository.findByName(userName)
                        .orElseThrow(() -> new UserNotFound(USER_WITH_SUCH_NAME_NOT_FOUND + userName)))
                .toList();
    }

    @Mapping(source = "divideBetween", target = "divideBetween", qualifiedByName = "mapUserListToUserNameList")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "payer", target = "payedBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "event", target = "eventId", qualifiedByName = "mapEventEntityToEventDto")
    ExpenseDto mapExpenseEntityToExpenseDto(Expense expense);

    @Named("mapUserListToUserNameList")
    default List<String> mapUserListToUserNameList(List<User> users) {

        if (users == null) {
            return null;
        }

        return users
                .stream()
                .map(User::getName)
                .toList();
    }

    @Named("mapEventEntityToEventDto")
    default Integer mapEventEntityToEventDto(Event event) {
        return event.getId();
    }
}