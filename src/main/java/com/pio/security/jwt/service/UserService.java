package com.pio.security.jwt.service;

import com.pio.security.jwt.constant.ErrorCode;
import com.pio.security.jwt.constant.ErrorMessage;
import com.pio.security.jwt.constant.ResponseMessage;
import com.pio.security.jwt.dto.UserDTO;
import com.pio.security.jwt.exception.UserAlreadyExistsException;
import com.pio.security.jwt.exception.UserNotFoundException;
import com.pio.security.jwt.model.UserEntity;
import com.pio.security.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * @return List of UserDTO
     */
    public List<UserDTO> getAllUser() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDTO> userDTOList = userEntityList.stream().map(userEntity -> new UserDTO(userEntity.getUserId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole())).collect(Collectors.toList());
        if (userDTOList.isEmpty()) {
            throw new UserNotFoundException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_ANY_USER_EXISTS);
        }
        return userDTOList;
    }

    /**
     * @param id
     * @return UserDTO
     */
    public UserDTO getUserById(int id) {
        UserEntity userEntity = null;
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userEntity = optionalUser.get();
            return new UserDTO(userEntity.getUserId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole());
        }
        throw new UserNotFoundException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND_ID + id);
    }

    /**
     * @param userDTO
     */
    public void addUser(UserDTO userDTO) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(ErrorCode.BAD_REQUEST, ErrorMessage.USER_ALREADY_TAKEN);
        }
        saveUser(userDTO);
    }

    private String saveUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setRole(userDTO.getRole());
        userRepository.save(userEntity);
        return ResponseMessage.USER_CREATED_SUCCESSFULLY;
    }
}
