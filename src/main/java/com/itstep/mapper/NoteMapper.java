package com.itstep.mapper;

import com.itstep.dto.NoteDto;
import com.itstep.entity.Note;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface NoteMapper {
    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserNameToUserEntity")
    Note toEntity(NoteDto noteDto, @Context UserRepository userRepository);

    @Mapping(source = "createdBy", target = "createdBy", qualifiedByName = "mapUserEntityToUserName")
    NoteDto toDto(Note note);
}
