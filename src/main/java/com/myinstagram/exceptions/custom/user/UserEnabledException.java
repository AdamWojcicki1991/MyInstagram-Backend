package com.myinstagram.exceptions.custom.user;

public class UserEnabledException extends RuntimeException {
    public UserEnabledException(String login) {
        super("User is already authorized login: " + login);
    }
}
