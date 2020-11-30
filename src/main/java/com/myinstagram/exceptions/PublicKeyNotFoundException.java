package com.myinstagram.exceptions;

public class PublicKeyNotFoundException extends RuntimeException {
    public PublicKeyNotFoundException(String message) {
        super("Exception occurred while retrieving public key from keystore with message: " + message);
    }
}
