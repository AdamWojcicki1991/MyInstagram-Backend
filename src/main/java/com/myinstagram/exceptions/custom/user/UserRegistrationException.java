package com.myinstagram.exceptions.custom.user;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String login, String email) {
        super("User exists and can not be created with login: " + login + " or User email: " + email + " is not valid!");
    }
}
