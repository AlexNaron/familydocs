package com.example.familydocs.exception;

public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String userName) {
        super("User with Name " + userName + " not found.");
    }
}
