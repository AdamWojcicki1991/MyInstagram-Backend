package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.mail.Mail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static com.myinstagram.util.TestDataFixture.createUser;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MailCreatorTestSuite {
    @Autowired
    MailCreationService mailCreationService;

    @Test
    public void shouldCreateNewUserEmail() {
        //GIVEN
        User user = createUser("login", "email@gmail.com");
        //WHEN
        String newUserEmailTemplate = mailCreationService.createNewUserEmail(user, "Password");
        //THEN
        assertNotNull(newUserEmailTemplate);
        assertTrue(newUserEmailTemplate.contains("MyInstagram - New User"));
    }

    @Test
    public void shouldCreateResetPasswordEmail() {
        //GIVEN
        User user = createUser("login", "email@gmail.com");
        //WHEN
        String resetPasswordEmailTemplate = mailCreationService.createResetPasswordEmail(user, "Password");
        //THEN
        assertNotNull(resetPasswordEmailTemplate);
        assertTrue(resetPasswordEmailTemplate.contains("MyInstagram - Reset Password"));
    }

    @Test
    public void shouldCreateUpdateUserProfileEmail() {
        //GIVEN
        User user = createUser("login", "email@gmail.com");
        //WHEN
        String updateUserProfileEmailTemplate = mailCreationService.createUpdateUserProfileEmail(user);
        //THEN
        assertNotNull(updateUserProfileEmailTemplate);
        assertTrue(updateUserProfileEmailTemplate.contains("MyInstagram - Update Password"));
    }

    @Test
    public void shouldCreateMimeMessage() {
        //GIVEN
        Mail mail = new Mail("email@gmail.com", "Test Subject", "Text Template");
        //WHEN
        MimeMessagePreparator mimeMessage = mailCreationService.createMimeMessage(mail);
        //THEN
        assertNotNull(mimeMessage);
    }
}
