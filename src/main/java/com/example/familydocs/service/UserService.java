package com.example.familydocs.service;

import com.example.familydocs.model.User;

import com.example.familydocs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user) {
        String encodedUserPassword = passwordEncoder.encode((user.getPassword()));
        user.setPassword(encodedUserPassword);
        return userRepository.save(user);
    }
}

