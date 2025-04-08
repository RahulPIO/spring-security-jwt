package com.pio.security.jwt.controller;

import com.pio.security.jwt.model.AuthRequest;
import com.pio.security.jwt.model.AuthResponse;
import com.pio.security.jwt.security.JwtHelper;
import com.pio.security.jwt.security.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final JwtUtility jwtUtility;
    private final JwtHelper jwtHelper;

    @Autowired
    public AuthController(UserDetailsService userDetailsService, JwtUtility jwtUtility, JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtUtility = jwtUtility;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        jwtUtility.doAuthenticate(authRequest.getUsername(), authRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = this.jwtHelper.generateToken(userDetails);
        AuthResponse response = new AuthResponse(token, userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
