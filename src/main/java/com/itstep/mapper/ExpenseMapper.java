package com.itstep.mapper;

import com.itstep.dto.ExpenseDto;
import com.itstep.entity.Expense;
import com.itstep.repository.EventRepository;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, SplitDetailsMapper.class, UserMapper.class, EventMapper.class})
public interface ExpenseMapper {
    @Mapping(source = "payedBy", target = "payer", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserNameToUserEntity")
    @Mapping(source = "eventId", target = "event", qualifiedByName = "mapEventIdToEventEntity")
    Expense toEntity(ExpenseDto expenseDto, @Context EventRepository eventRepository,
                     @Context UserRepository userRepository);

    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "payer", target = "payedBy", qualifiedByName = "mapUserEntityToUserName")
    @Mapping(source = "event", target = "eventId", qualifiedByName = "mapEventEntityToEventId")
    ExpenseDto toDto(Expense expense);
}