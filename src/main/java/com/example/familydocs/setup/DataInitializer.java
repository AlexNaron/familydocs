package com.example.familydocs.setup;

import com.example.familydocs.model.Role;
import com.example.familydocs.model.User;
import com.example.familydocs.repository.UserRepository;
import com.example.familydocs.service.RoleService;
import com.example.familydocs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!userService.existsByUsername("admin")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin"));

            Role adminRole = roleService.getRoleByName("ROLE_ADMIN");
            adminUser.setRoles(Collections.singleton(adminRole));
            userRepository.save(adminUser);
        }
    }
}
