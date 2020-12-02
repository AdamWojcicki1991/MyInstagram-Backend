package com.myinstagram.exceptions.custom.user;

public class UserFoundException extends RuntimeException {
    public UserFoundException(String login) {
        super("User exists and can not be created with login: " + login);
    }
}
