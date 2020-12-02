package com.myinstagram.exceptions.custom.mail;

public class MailSenderException extends RuntimeException {
    public MailSenderException() {
        super("Failed to process email sending!");
    }
}
