package com.myinstagram.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("Could not find user by login: " + login);
    }
}
