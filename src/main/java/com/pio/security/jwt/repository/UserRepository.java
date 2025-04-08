package com.pio.security.jwt.repository;

import com.pio.security.jwt.constant.UserRole;
import com.pio.security.jwt.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByRole(UserRole role);
}
