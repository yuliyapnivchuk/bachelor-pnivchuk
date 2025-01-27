package com.itstep.mapper;

import com.itstep.dto.ItemDto;
import com.itstep.entity.Expense;
import com.itstep.entity.Item;
import com.itstep.entity.User;
import com.itstep.exception.UserNotFound;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = SplitDetailsMapper.class)
public interface ItemMapper {

    @Mapping(target = "expense", ignore = true)
    Item toEntityIgnoreExpense(ItemDto itemDto, @Context UserRepository userRepository);

    ItemDto toDto(Item item);

    default List<Item> mapDtoListToEntityList(List<ItemDto> itemDtoList, @Context Expense expense, @Context UserRepository userRepository) {
        return itemDtoList.stream()
                .map(itemDto -> toEntity(itemDto, expense, userRepository))
                .toList();
    }

    default Item toEntity(ItemDto itemDto, @Context Expense expense, @Context UserRepository userRepository) {
        Item item = toEntityIgnoreExpense(itemDto, userRepository);
        item.setExpense(expense);
        return item;
    }
}