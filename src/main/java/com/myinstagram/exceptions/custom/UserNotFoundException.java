package com.myinstagram.exceptions.custom;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("Could not find user by: " + message);
    }
}
