package com.example.familydocs.controller;

import com.example.familydocs.model.User;
import com.example.familydocs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/hello")
    public String sayHello(@AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        return "Hello, " + username;
    }
}
