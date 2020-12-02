package com.myinstagram.exceptions.custom.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Could not find post by id: " + id);
    }
}
