package com.myinstagram.repository;

import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.mail.Mail;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Repository;

@Repository
public interface MailCreatorRepository {
    String createNewUserEmail(final User user, final String password);

    String createResetPasswordEmail(final User user, final String password);

    String createUpdateUserProfileEmail(final User user);

    MimeMessagePreparator createMimeMessage(final Mail mail);
}
