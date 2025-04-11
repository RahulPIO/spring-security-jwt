package com.pio.security.jwt.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void generateTokenTest() {
        when(jwtHelper.generateToken(userDetails)).thenReturn("asdvsadvadveradfsdfvad32r23rcvsdv");
        String token = jwtHelper.generateToken(userDetails);
        assertEquals("asdvsadvadveradfsdfvad32r23rcvsdv", token);
    }

    @Test
    void isTokenValidatedTrue() {
        when(jwtHelper.isTokenValidated("asdvsadvadveradfsdfvad32r23rcvsdv",userDetails)).thenReturn(true);
        boolean isValidated = jwtHelper.isTokenValidated("asdvsadvadveradfsdfvad32r23rcvsdv",userDetails);
        assertTrue(isValidated);
    }

    @Test
    void isTokenValidatedFalse() {
        boolean isValidated = jwtHelper.isTokenValidated("asdvsadvadveradfsdfvad32r23rcvsd",userDetails);
        assertFalse(isValidated);
    }
}
