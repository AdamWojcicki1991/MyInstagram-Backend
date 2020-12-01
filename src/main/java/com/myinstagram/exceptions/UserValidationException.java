package com.myinstagram.exceptions;

import com.myinstagram.domain.enums.ValidationStatus;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String login, ValidationStatus validationStatus) {
        super("User " + login + validationStatus.toString());
    }
}
