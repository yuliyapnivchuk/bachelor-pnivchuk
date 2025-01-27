package com.itstep.mapper;

import com.itstep.dto.SplitDetailsDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.entity.SplitDetails;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface SplitDetailsMapper {
    @Mapping(target = "expense", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(source = "user", target = "user", qualifiedByName = "mapUserNameToUserEntity")
    SplitDetails toEntityIgnoreExpenseItem(SplitDetailsDto splitDetailsDto, @Context UserRepository userRepository);

    default SplitDetails toEntity(SplitDetailsDto splitDetailsDto, @Context Expense expense, @Context Item item,
                                  @Context UserRepository userRepository) {
        SplitDetails splitDetails = toEntityIgnoreExpenseItem(splitDetailsDto, userRepository);
        splitDetails.setExpense(expense);
        splitDetails.setItem(item);
        return splitDetails;
    }

    @Named("mapSplitExpenseDtoListToSplitExpenseEntityList")
    default List<SplitDetails> mapSplitExpenseDtoListToSplitExpenseEntityList(List<SplitDetailsDto> splitDetailsDtoList,
                                                                              @Context Expense expense, @Context Item item,
                                                                              @Context UserRepository userRepository) {
        return splitDetailsDtoList.stream()
                .map(splitDetailsDto -> toEntity(splitDetailsDto, expense, item, userRepository))
                .toList();
    }

    @Mapping(source = "user", target = "user", qualifiedByName = "mapUserEntityToUserName")
    SplitDetailsDto toDto(SplitDetails splitDetails);
}
