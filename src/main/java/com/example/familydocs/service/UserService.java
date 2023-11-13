package com.example.familydocs.service;

import com.example.familydocs.exception.UsernameNotFoundException;
import com.example.familydocs.model.Role;
import com.example.familydocs.model.User;

import com.example.familydocs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleService roleService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User addUser(User user) {

        String encodedUserPassword = passwordEncoder.encode((user.getPassword()));
        user.setPassword(encodedUserPassword);

        Role role = roleService.getRoleByName("ROLE_USER");
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    public User changeUserPassword(String username, String newPassword){

        User user = userRepository.findByUsername(username);
        if (user == null) { throw new UsernameNotFoundException(username);}
        user.setPassword(newPassword);

        return userRepository.save(user);

    }

    public boolean existsByUsername(String username) {

        User user = userRepository.findByUsername(username);

        return user != null;
    }
}

