package com.myinstagram.exceptions;

public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(Long id) {
        super("Could not find user by id: " + id);
    }
}
