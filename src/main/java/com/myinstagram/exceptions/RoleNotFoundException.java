package com.myinstagram.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Could not find role by id: " + id);
    }
}
