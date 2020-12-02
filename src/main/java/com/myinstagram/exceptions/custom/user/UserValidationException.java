package com.myinstagram.exceptions.custom.user;

import com.myinstagram.domain.enums.ValidationStatus;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String login, ValidationStatus validationStatus) {
        super("User " + login + validationStatus.toString());
    }
}
