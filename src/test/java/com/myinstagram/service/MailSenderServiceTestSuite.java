package com.myinstagram.service;

import com.myinstagram.domain.mail.Mail;
import com.myinstagram.exceptions.custom.mail.MailSenderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.myinstagram.util.DomainDataFixture.createMail;
import static com.myinstagram.util.DomainDataFixture.createSimpleMailMessage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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
        Mail mail = createMail();
        SimpleMailMessage mailMessage = createSimpleMailMessage(mail);
        //WHEN
        mailSenderService.sendEmail(mail);
        //THEN
        verify(javaMailSender, times(0)).send(mailMessage);
    }

    @Test
    public void shouldNotSendEmailAndThrowMailSenderException() {
        //GIVEN
        Mail mail = createMail();
        createSimpleMailMessage(mail);
        given(mailCreationService.createMimeMessage(mail)).willThrow(MailSenderException.class);
        //WHEN & THEN
        assertThatThrownBy(() -> mailSenderService.sendEmail(mail))
                .isInstanceOf(MailSenderException.class)
                .hasMessage("Failed to process email sending!");
    }

    @Test
    public void shouldSendPersonalizedEmail() {
        //GIVEN
        Mail mail = createMail();
        SimpleMailMessage mailMessage = createSimpleMailMessage(mail);
        //WHEN
        mailSenderService.sendPersonalizedEmail(mail.getMailTo(), mail.getSubject(), mail.getText());
        //THEN
        verify(javaMailSender, times(0)).send(mailMessage);
    }
}
