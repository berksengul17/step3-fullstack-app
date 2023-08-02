package com.berk.server.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This annotation provides a lightweight configuration for testing JPA repositories.
// It will set up an in-memory H2 database and automatically configure Spring Data JPA for testing.
@DataJpaTest
class UserRepositoryTest {

    // Spring will automatically look for a bean that
    // matches the type of the field or parameter and inject it into the class.
    @Autowired
    private UserRepository userRepository;

    @Test
    void testExistingUserName() {
        String name = "Existing User";
        User user = new User(1L, name);

        userRepository.save(user);
        boolean expected = userRepository.existsByName(name);

        assertTrue(expected);
    }

    @Test
    void testNonExistingUserName() {
        String name = "Nonexistent User";

        boolean expected = userRepository.existsByName(name);

        assertFalse(expected);
    }
}