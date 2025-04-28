package com.pio.security.jwt.service;

import com.pio.security.jwt.constant.ConstantTest;
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
        userDTO.setUsername(ConstantTest.TEST_USERNAME);
        userDTO.setPassword(ConstantTest.TEST_PASSWORD);
        userDTO.setRole(UserRole.ADMIN);
    }

    @Test
    void shouldAddUserSuccess() {
        when(userRepository.findByUsername(ConstantTest.TEST_USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(ConstantTest.TEST_PASSWORD)).thenReturn("abc1234");
        userService.addUser(userDTO);
        verify(userRepository).save(userEntityCaptor.capture());
        UserEntity savedUser = userEntityCaptor.getValue();
        assertEquals(ConstantTest.TEST_USERNAME,savedUser.getUsername());
        assertEquals("abc1234",savedUser.getPassword());
        assertEquals(UserRole.ADMIN,savedUser.getRole());
        assertNotNull(savedUser.getPassword());
        assertNotEquals("",savedUser.getUsername());
    }

    @Test
    void shouldAddUserFailed() {
        when(userRepository.findByUsername(ConstantTest.TEST_USERNAME)).thenThrow(new RuntimeException(ConstantTest.ERROR_USERNAME_TAKEN));
        RuntimeException exceptionThrown = assertThrows(RuntimeException.class,() -> {
            userService.addUser(userDTO);
                });
        assertEquals(ConstantTest.ERROR_USERNAME_TAKEN,exceptionThrown.getMessage());
        assertThrowsExactly(RuntimeException.class,() -> {
            userService.addUser(userDTO);
        });
    }

    @Test
    void shouldGetUserByIdSuccess() {
        UserEntity userEntity = new UserEntity(ConstantTest.TEST_USER_ID, ConstantTest.TEST_USERNAME, ConstantTest.TEST_PASSWORD, UserRole.ADMIN);
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));
        UserDTO userResult = userService.getUserById(1);
        assertEquals(ConstantTest.TEST_USERNAME,userResult.getUsername());
        assertEquals(UserRole.ADMIN,userResult.getRole());
    }

    @Test
    void shouldGetUserByIdFailed() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        UserNotFoundException exceptionThrown = assertThrows(UserNotFoundException.class, () ->{
                    userService.getUserById(1);
                });
        assertEquals(404,exceptionThrown.getHttpStatus());
        assertEquals(ConstantTest.USER_NOT_FOUND +1,exceptionThrown.getMessage());
    }

    @Test
    void shouldGetAllUserSuccess() {
        List<UserEntity> userEntityList = Arrays.asList(new UserEntity(ConstantTest.TEST_USER_ID, ConstantTest.TEST_USERNAME, ConstantTest.TEST_PASSWORD,UserRole.USER),new UserEntity(2,"xyz","789",UserRole.USER));
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
        assertEquals(ConstantTest.NOT_ANY_USER_EXIST,thrownException.getMessage());
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
        assertEquals(ConstantTest.USER_ADDED_SUCCESSFULLY,message);
    }

    @Test
    void testSaveUserUsingPowermock() throws Exception {
        String message = Whitebox.invokeMethod(userService,"saveUser",userDTO);
        assertEquals(ConstantTest.USER_ADDED_SUCCESSFULLY,message);
    }
}
