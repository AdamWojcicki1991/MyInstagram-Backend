package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.mail.Mail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MailSenderServiceTestSuite {
    @InjectMocks
    private MailSenderService mailSenderService;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private MailCreationService mailCreationService;

    @Test
    public void shouldSendEmail() {
        //GIVEN
        Mail mail = new Mail("email@gmail.com", "Test Subject",
                             mailCreationService.createNewUserEmail(
                                     User.builder().build(), "Password"));
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());
        //WHEN
        mailSenderService.sendEmail(mail);
        //THEN
        verify(javaMailSender, times(0)).send(mailMessage);
    }
}
