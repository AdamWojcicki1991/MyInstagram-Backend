package com.myinstagram.exceptions.custom.security;

public class ChangePasswordException extends RuntimeException {
    public ChangePasswordException() {
        super("Error occurred during changing password process!");
    }
}
