package com.myinstagram.exceptions.custom.comment;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("Could not find comment by id: " + id);
    }
}
