package com.pio.security.jwt.security;

import com.pio.security.jwt.constant.ConstantTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class JwtHelperTest {

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserDetails userDetails;

    @Test
    void shouldGenerateTokenTest() {
        when(jwtHelper.generateToken(userDetails)).thenReturn(ConstantTest.TEST_JWT_TOKEN);
        String token = jwtHelper.generateToken(userDetails);
        assertEquals(ConstantTest.TEST_JWT_TOKEN, token);
    }

    @Test
    void shouldIsTokenValidatedTrue() {
        when(jwtHelper.isTokenValidated(ConstantTest.TEST_JWT_TOKEN, userDetails)).thenReturn(true);
        boolean isValidated = jwtHelper.isTokenValidated(ConstantTest.TEST_JWT_TOKEN, userDetails);
        assertTrue(isValidated);
    }

    @Test
    void shouldIsTokenValidatedFalse() {
        boolean isValidated = jwtHelper.isTokenValidated("asdvsadvadveradfsdfvad32r23rcvsd", userDetails);
        assertFalse(isValidated);
    }
}
