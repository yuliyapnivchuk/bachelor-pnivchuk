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

import static com.itstep.exception.ConstantsUtility.USER_WITH_SUCH_NAME_NOT_FOUND;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "expense", ignore = true)
    @Mapping(source = "assignedTo", target = "assignedTo", qualifiedByName = "mapUserNameToUserEntity")
    Item mapItemDtoToItemEntityWithoutExpense(ItemDto itemDto, @Context UserRepository userRepository);

    @Named("mapItemDtoListToItemEntityList")
    default List<Item> mapItemDtoListToItemEntityList(List<ItemDto> itemDtoList, @Context Expense expense, @Context UserRepository userRepository) {
        return itemDtoList.stream()
                .map(itemDto -> mapItemDtoToItemEntity(itemDto, expense, userRepository))
                .toList();
    }

    default Item mapItemDtoToItemEntity(ItemDto itemDto, @Context Expense expense, @Context UserRepository userRepository) {
        Item item = mapItemDtoToItemEntityWithoutExpense(itemDto, userRepository);
        item.setExpense(expense);
        return item;
    }

    @Named("mapUserNameToUserEntity")
    default User mapUserNameToUserEntity(String userName, @Context UserRepository userRepository) {

        if (userName == null) {
            return null;
        }

        return userRepository.findByName(userName)
                .orElseThrow(() -> new UserNotFound(USER_WITH_SUCH_NAME_NOT_FOUND + userName));
    }

    @Mapping(source = "assignedTo", target = "assignedTo", qualifiedByName = "mapUserEntityToUserName")
    ItemDto mapItemEntityToItemDto(Item item);

    @Named("mapUserEntityToUserName")
    default String mapUserEntityToUserName(User user) {
        return (user == null) ? null : user.getName();
    }
}