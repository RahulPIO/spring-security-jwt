package com.pio.security.jwt.service;

import com.pio.security.jwt.constant.UserRole;
import com.pio.security.jwt.dto.UserDTO;
import com.pio.security.jwt.exception.UserNotFoundException;
import com.pio.security.jwt.model.UserEntity;
import com.pio.security.jwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(value={MockitoExtension.class})
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("abc");
        userDTO.setPassword("1234");
        userDTO.setRole(UserRole.ADMIN);
    }

    @Test
    void testAddUserSuccess() {
        when(userRepository.findByUsername("abc")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("abc1234");
        userService.addUser(userDTO);
        verify(userRepository).save(userEntityCaptor.capture());
        UserEntity savedUser = userEntityCaptor.getValue();
        assertEquals("abc",savedUser.getUsername());
        assertEquals("abc1234",savedUser.getPassword());
        assertEquals(UserRole.ADMIN,savedUser.getRole());
    }

    @Test
    void testAddUserFailed() {
        when(userRepository.findByUsername("abc")).thenThrow(new RuntimeException("Username already taken"));
        RuntimeException exceptionThrown = assertThrows(RuntimeException.class,() -> {
            userService.addUser(userDTO);
                });
        assertEquals("Username already taken",exceptionThrown.getMessage());
        assertThrowsExactly(RuntimeException.class,() -> {
            userService.addUser(userDTO);
        });
    }

    @Test
    void testGetUserByIdSuccess() {
        UserEntity userEntity = new UserEntity(1,"abc","abc1234", UserRole.ADMIN);
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        UserDTO userResult = userService.getUserById(1);
        assertEquals("abc",userResult.getUsername());
        assertEquals(UserRole.ADMIN,userResult.getRole());
    }

    @Test
    void testGetUserByIdFailed() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        UserNotFoundException exceptionThrown = assertThrows(UserNotFoundException.class, () ->{
                    userService.getUserById(1);
                });
        assertEquals(404,exceptionThrown.getHttpStatus());
        assertEquals("User not found with id " +1,exceptionThrown.getMessage());
    }

    @Test
    void testGetAllUserSuccess() {
        List<UserEntity> userEntityList = Arrays.asList(new UserEntity(1,"abc","123",UserRole.USER),new UserEntity(2,"xyz","789",UserRole.USER));
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<UserDTO> userDTOList = userService.getAllUser();
        assertNotNull(userDTOList);
        assertThat(userDTOList).hasSize(2);
    }

    @Test
    void testGetAllUserFailed() {
        when(userRepository.findAll()).thenReturn(List.of());
        UserNotFoundException thrownException = assertThrows(UserNotFoundException.class, () -> {
            userService.getAllUser();
        });
        assertEquals("Not any user exists in Database",thrownException.getMessage());
        assertEquals(404,thrownException.getHttpStatus());
    }

    @Test
    void testSaveUserUsingReflection() throws Exception {
        UserEntity user = new UserEntity(userDTO.getId(),userDTO.getUsername(),userDTO.getPassword(),userDTO.getRole());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("abc123");
        Class[] parameters = new Class[1];
        parameters[0] = UserDTO.class;

        Method methodCall = userService.getClass().getDeclaredMethod("saveUser",parameters);
        methodCall.setAccessible(true);
        Object[] methodArgument = new Object[1];
        methodArgument[0] = userDTO;
        String message = (String)methodCall.invoke(userService,methodArgument);
        assertEquals("User Added Successfully",message);
    }

    @Test
    void testSaveUserUsingPowermock() throws Exception {
        String message = Whitebox.invokeMethod(userService,"saveUser",userDTO);
        assertEquals("User Added Successfully",message);
    }
}
