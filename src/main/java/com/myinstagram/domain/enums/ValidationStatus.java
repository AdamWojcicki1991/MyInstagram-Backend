package com.myinstagram.domain.enums;

public enum ValidationStatus {
    AUTHORIZED(" is not authorized or active!"),
    AUTHORIZED_CONTAINS_EMAIL(" is not authorized or new email is already occupied!"),
    AUTHORIZED_CONTAINS_POST_LIKED(" is not authorized or post is already liked by this user!"),
    AUTHORIZED_CONTAINS_POST_UNLIKED(" is not authorized or post is already unliked by this user!");

    ValidationStatus(String message) {
    }
}
