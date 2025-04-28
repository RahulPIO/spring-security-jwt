package com.pio.security.jwt.controller;

import com.pio.security.jwt.constant.ResponseMessage;
import com.pio.security.jwt.dto.UserDTO;
import com.pio.security.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody UserDTO user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMessage.USER_CREATED_SUCCESSFULLY);
    }

    @GetMapping
    @PreAuthorize(value = "hasRole('ROLE_Admin')")
    public ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
