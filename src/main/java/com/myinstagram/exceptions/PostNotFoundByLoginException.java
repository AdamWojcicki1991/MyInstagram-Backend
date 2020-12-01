package com.myinstagram.exceptions;

public class PostNotFoundByLoginException extends RuntimeException {
    public PostNotFoundByLoginException(String login) {
        super("Could not find posts by login: " + login);
    }
}
