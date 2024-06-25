package com.sparta.catube.controller;

import com.sparta.catube.dto.UserDto;
import com.sparta.catube.entity.User;
import com.sparta.catube.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        User user = userService.registerUser(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto) {
        String token = userService.loginUser(userDto);
        if (token != null) {
            log.info("login success");
        }
        return ResponseEntity.ok(token);
    }

}

