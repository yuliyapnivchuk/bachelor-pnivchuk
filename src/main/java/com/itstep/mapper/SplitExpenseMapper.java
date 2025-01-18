package com.itstep.mapper;

import com.itstep.dto.SplitExpenseDto;
import com.itstep.entity.Expense;
import com.itstep.entity.SplitExpense;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface SplitExpenseMapper {
    @Mapping(target = "expense", ignore = true)
    @Mapping(source = "user", target = "user", qualifiedByName = "mapUserNameToUserEntity")
    SplitExpense toEntityIgnoreExpense(SplitExpenseDto splitExpenseDto, @Context UserRepository userRepository);

    default SplitExpense toEntity(SplitExpenseDto splitExpenseDto, @Context Expense expense,
                                  @Context UserRepository userRepository) {
        SplitExpense splitExpense = toEntityIgnoreExpense(splitExpenseDto, userRepository);
        splitExpense.setExpense(expense);
        return splitExpense;
    }

    @Named("mapSplitExpenseDtoListToSplitExpenseEntityList")
    default List<SplitExpense> mapSplitExpenseDtoListToSplitExpenseEntityList(List<SplitExpenseDto> splitExpenseDtoList,
                                                                              @Context Expense expense,
                                                                              @Context UserRepository userRepository) {
        return splitExpenseDtoList.stream()
                .map(splitExpenseDto -> toEntity(splitExpenseDto, expense, userRepository))
                .toList();
    }

    @Mapping(source = "user", target = "user", qualifiedByName = "mapUserEntityToUserName")
    SplitExpenseDto toDto(SplitExpense splitExpense);
}
