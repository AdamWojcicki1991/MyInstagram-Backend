package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.mail.Mail;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static com.myinstagram.util.DataFixture.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MailCreationServiceTestSuite {
    @Autowired
    private MailCreationService mailCreationService;

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
        assertTrue(updateUserProfileEmailTemplate.contains("MyInstagram - Update Profile"));
    }

    @Test
    public void shouldCreateWeatherEmail() {
        //GIVEN
        User user = createUser("login", "email@gmail.com");
        OpenWeatherResponseDto openWeatherResponseDto = createOpenWeatherResponseDto();
        //WHEN
        String weatherEmailTemplate = mailCreationService.createWeatherEmail(user, openWeatherResponseDto);
        //THEN
        assertNotNull(weatherEmailTemplate);
        assertTrue(weatherEmailTemplate.contains("MyInstagram - Weather Email"));
    }

    @Test
    public void shouldCreateMimeMessage() {
        //GIVEN
        Mail mail = createMail();
        //WHEN
        MimeMessagePreparator mimeMessage = mailCreationService.createMimeMessage(mail);
        //THEN
        assertNotNull(mimeMessage);
    }

    @Test
    public void shouldCreateMail() {
        //GIVEN
        Mail mail = createMail();
        //WHEN
        Mail createdMail = mailCreationService.createMail(mail.getMailTo(), mail.getSubject(), mail.getText());
        //THEN
        assertNotNull(createdMail);
        assertEquals(mail, createdMail);
    }
}
