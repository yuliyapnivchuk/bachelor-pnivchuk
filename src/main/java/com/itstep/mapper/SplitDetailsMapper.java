package com.itstep.mapper;

import com.itstep.dto.SplitDetailsDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.entity.SplitDetails;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface SplitDetailsMapper {
    @Mapping(target = "expense", ignore = true)
    @Mapping(target = "item", ignore = true)
    SplitDetails toEntityIgnoreExpenseItem(SplitDetailsDto splitDetailsDto);

    default SplitDetails toEntity(SplitDetailsDto splitDetailsDto, @Context Expense expense, @Context Item item) {
        SplitDetails splitDetails = toEntityIgnoreExpenseItem(splitDetailsDto);
        splitDetails.setExpense(expense);
        splitDetails.setItem(item);
        return splitDetails;
    }

    SplitDetailsDto toDto(SplitDetails splitDetails);
}
