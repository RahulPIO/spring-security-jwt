package com.pio.security.jwt.repository;

import com.pio.security.jwt.constant.UserRole;
import com.pio.security.jwt.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = new UserEntity();
        user.setUsername("Dhanraj");
        user.setPassword("J1234");
        user.setRole(UserRole.USER);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        UserEntity resultUser = userRepository.save(user);
        assertEquals(user.getUsername(),resultUser.getUsername());
        assertEquals(user.getPassword(),resultUser.getPassword());
    }

    @Test
    void shouldFindByUsernameSuccessfully() {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        assertTrue(optionalUser.isPresent());
        UserEntity resultUser = optionalUser.get();
        assertEquals(user.getUsername(),resultUser.getUsername());
        assertEquals(user.getPassword(),resultUser.getPassword());
    }

    @Test
    void shouldFindByRoleSuccessfully() {
        List<UserEntity> userEntityList = userRepository.findByRole(UserRole.ADMIN);
        assertTrue(!userEntityList.isEmpty());
    }

}
