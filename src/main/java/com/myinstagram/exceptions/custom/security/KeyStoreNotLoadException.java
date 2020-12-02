package com.myinstagram.exceptions.custom.security;

public class KeyStoreNotLoadException extends RuntimeException {
    public KeyStoreNotLoadException(String message) {
        super("Exception occurred while loading keystore with message: " + message);
    }
}
