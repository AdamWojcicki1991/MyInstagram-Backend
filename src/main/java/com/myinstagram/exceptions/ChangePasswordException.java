package com.myinstagram.exceptions;

public class ChangePasswordException extends RuntimeException {
    public ChangePasswordException() {
        super("Error occurred during changing password process!");
    }
}
