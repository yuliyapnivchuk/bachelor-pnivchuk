package com.itstep.repository;

import com.itstep.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    void findByNameTest() {
        User user = User.builder().name("user1").email("user1@gmail.com").build();
        testEntityManager.persist(user);

        Optional<User> result = userRepository.findByName("user1");

        assertThat(result).isPresent();
        assertThat(result).contains(user);
    }
}
