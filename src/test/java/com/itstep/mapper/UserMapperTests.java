package com.itstep.mapper;

import com.itstep.exception.UserNotFound;
import com.itstep.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = MapperTestConfig.class)
public class UserMapperTests {

    @Autowired
    UserMapper userMapper;

    @MockitoBean
    UserRepository userRepository;

    @Test
    void mapUserNameToUserEntityUserNotFoundTest() {
        when(userRepository.findByName(anyString())).thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFound.class, () -> userMapper.mapUserNameToUserEntity("user1", userRepository));
    }
}
