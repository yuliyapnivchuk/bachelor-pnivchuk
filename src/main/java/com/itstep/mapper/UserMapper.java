package com.itstep.mapper;

import com.itstep.entity.User;
import com.itstep.exception.UserNotFound;
import com.itstep.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import static com.itstep.exception.ConstantsUtility.USER_WITH_SUCH_NAME_NOT_FOUND;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Named("mapUserNameToUserEntity")
    default User mapUserNameToUserEntity(String userName, @Context UserRepository userRepository) {

        if (userName == null) {
            return null;
        }

        return userRepository.findByName(userName)
                .orElseThrow(() -> new UserNotFound(USER_WITH_SUCH_NAME_NOT_FOUND + userName));
    }

    @Named("mapUserEntityToUserName")
    default String mapUserEntityToUserName(User user) {
        return (user == null) ? null : user.getName();
    }
}
