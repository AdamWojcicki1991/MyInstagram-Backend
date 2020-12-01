package com.myinstagram.exceptions;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String login) {
        super("User " + login + " is not authorized, active or can not vote again!");
    }
}
