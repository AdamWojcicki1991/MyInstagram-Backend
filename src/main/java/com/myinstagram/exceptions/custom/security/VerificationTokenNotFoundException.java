package com.myinstagram.exceptions.custom.security;

public class VerificationTokenNotFoundException extends RuntimeException {
    public VerificationTokenNotFoundException(String token) {
        super("Could not find verification token by token: " + token);
    }
}
