package com.myinstagram.repository;

import com.myinstagram.domain.mail.Mail;
import org.springframework.stereotype.Repository;

@Repository
public interface MailSenderRepository {
    void sendEmail(final Mail mail);
}
