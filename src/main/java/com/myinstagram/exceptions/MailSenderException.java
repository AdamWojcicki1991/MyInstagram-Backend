package com.myinstagram.exceptions;

public class MailSenderException extends RuntimeException {
    public MailSenderException() {
        super("Failed to process email sending!");
    }
}
