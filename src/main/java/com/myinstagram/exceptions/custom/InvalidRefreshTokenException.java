package com.myinstagram.exceptions.custom;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String token) {
        super("Invalid refresh token passed: " + token);
    }
}
