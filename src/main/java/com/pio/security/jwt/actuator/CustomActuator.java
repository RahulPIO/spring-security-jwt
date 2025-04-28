package com.pio.security.jwt.actuator;

import com.pio.security.jwt.constant.ErrorCode;
import com.pio.security.jwt.constant.ErrorMessage;
import com.pio.security.jwt.constant.ResponseMessage;
import com.pio.security.jwt.constant.UserRole;
import com.pio.security.jwt.model.UserEntity;
import com.pio.security.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@Endpoint(id = "customEndpoint")
public class CustomActuator {

    private final UserRepository userRepository;

    @Autowired
    public CustomActuator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ReadOperation
    public List<UserEntity> readOperation(@Selector UserRole role) {
        return userRepository.findByRole(role);
    }

    @WriteOperation
    public UserEntity writeEndpoint(@Selector int id) {
        UserEntity userEntity = null;
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException(ErrorMessage.USER_NOT_FOUND_ID + id);
        }
        userEntity = optionalUser.get();
        userEntity.setRole(UserRole.USER);
        return userRepository.save(userEntity);
    }

    @DeleteOperation
    public String deleteOperation(@Selector int id) throws Exception {
        try {
            userRepository.deleteById(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return ResponseMessage.USER_DELETED_SUCCESSFULLY;
    }

}
