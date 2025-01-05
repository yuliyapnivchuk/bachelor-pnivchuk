package com.itstep.mapper;

import com.itstep.dto.SplitItemDto;
import com.itstep.entity.Item;
import com.itstep.entity.SplitItem;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface SplitItemMapper {
    @Mapping(target = "item", ignore = true)
    @Mapping(source = "user", target = "user", qualifiedByName = "mapUserNameToUserEntity")
    SplitItem toEntityIgnoreExpense(SplitItemDto splitItemDto, @Context UserRepository userRepository);

    default SplitItem toEntity(SplitItemDto splitItemDto, @Context Item item,
                               @Context UserRepository userRepository) {
        SplitItem splitItem = toEntityIgnoreExpense(splitItemDto, userRepository);
        splitItem.setItem(item);
        return splitItem;
    }

    @Named("mapSplitItemDtoListToSplitItemEntityList")
    default List<SplitItem> mapSplitItemDtoListToSplitItemEntityList(List<SplitItemDto> splitItemDtoList,
                                                                     @Context Item item,
                                                                     @Context UserRepository userRepository) {
        return splitItemDtoList.stream()
                .map(splitItemDto -> toEntity(splitItemDto, item, userRepository))
                .toList();
    }

    @Mapping(source = "user", target = "user", qualifiedByName = "mapUserEntityToUserName")
    SplitItemDto toDto(SplitItem splitItem);
}
