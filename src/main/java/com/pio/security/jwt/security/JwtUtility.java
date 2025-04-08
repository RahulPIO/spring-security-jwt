package com.pio.security.jwt.security;

import com.pio.security.jwt.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtUtility {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtility(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(400,"Invalid Credentials");
        }
    }
}
