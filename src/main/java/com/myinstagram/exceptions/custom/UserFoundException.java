package com.myinstagram.exceptions.custom;

public class UserFoundException extends RuntimeException {
    public UserFoundException(String login) {
        super("User exists and can not be created with login: " + login);
    }
}
