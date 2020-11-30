package com.myinstagram.exceptions;

public class KeyStoreNotLoadException extends RuntimeException {
    public KeyStoreNotLoadException(String message) {
        super("Exception occurred while loading keystore with message: " + message);
    }
}
