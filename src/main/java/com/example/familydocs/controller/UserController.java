package com.example.familydocs.controller;

import com.example.familydocs.logging.LoggableParameter;
import com.example.familydocs.model.User;
import com.example.familydocs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {

        return ResponseEntity.ok(userService.addUser(user));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @PostMapping
    public ResponseEntity<String> changePassword(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestBody String newPassword){

        User user = userService.changeUserPassword(userDetails.getUsername(), newPassword);

        return ResponseEntity.ok(user.getUsername());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(@LoggableParameter @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        return ResponseEntity.ok("Hello, " + username);
    }
}
