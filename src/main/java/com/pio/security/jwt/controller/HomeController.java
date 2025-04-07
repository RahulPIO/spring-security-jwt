package com.pio.security.jwt.controller;

import com.pio.security.jwt.dto.UserDTO;
import com.pio.security.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<UserDTO> getUser() {
        return userService.getAllUser();
    }
}
