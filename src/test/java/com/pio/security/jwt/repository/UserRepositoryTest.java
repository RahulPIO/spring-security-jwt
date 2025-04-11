package com.pio.security.jwt.repository;

import com.pio.security.jwt.constant.UserRole;
import com.pio.security.jwt.model.UserEntity;
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

    @Test
    void testSave() {
        UserEntity user = new UserEntity();
        user.setUsername("Dhanraj");
        user.setPassword("J1234");
        user.setRole(UserRole.USER);
        UserEntity resultUser = userRepository.save(user);
        assertEquals(user.getUsername(),resultUser.getUsername());
        assertEquals(user.getPassword(),resultUser.getPassword());
    }

    @Test
    void testFindByUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("nitin");
        user.setPassword("n1234");
        user.setRole(UserRole.USER);

        Optional<UserEntity> optionalUser = userRepository.findByUsername(user.getUsername());
        assertTrue(optionalUser.isPresent());
        UserEntity resultUser = optionalUser.get();
        assertEquals(user.getUsername(),resultUser.getUsername());
        assertEquals(user.getPassword(),resultUser.getPassword());
    }

    @Test
    void testFindByRole() {
        List<UserEntity> userEntityList = userRepository.findByRole(UserRole.ADMIN);
        assertTrue(!userEntityList.isEmpty());
    }

}
