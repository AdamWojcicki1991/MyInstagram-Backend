package com.myinstagram.service;

import com.myinstagram.domain.mail.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailSenderService {
    private final MailCreationService mailCreationService;
    private final JavaMailSender javaMailSender;

    public void sendEmail(final Mail mail) {
        log.info("Starting email preparation...");
        try {
            javaMailSender.send(mailCreationService.createMimeMessage(mail));
            log.info("Email has been sent.");
        } catch (MailException e) {
            log.error("Failed to process email sending: ", e.getMessage(), e);
        }
    }

    public void sendPersonalizedEmail(final String email, final String subject, final String text) {
        sendEmail(mailCreationService.createMail(email, subject, text));
    }
}