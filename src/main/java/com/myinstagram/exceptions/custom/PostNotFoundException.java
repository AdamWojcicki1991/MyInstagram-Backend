package com.myinstagram.exceptions.custom;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Could not find post by id: " + id);
    }
}