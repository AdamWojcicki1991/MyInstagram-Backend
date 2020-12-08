package com.myinstagram.util;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.mail.Mail;
import com.myinstagram.openweather.dto.OpenWeatherMainDto;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherWeatherDto;
import org.springframework.mail.SimpleMailMessage;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.SECONDS;

public final class DomainDataFixture {

    private DomainDataFixture() {
    }

    public static Mail createMail() {
        return new Mail.MailBuilder()
                .mailTo("email@gmail.com")
                .subject("Test Subject")
                .text("Test text")
                .build();
    }

    public static SimpleMailMessage createSimpleMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());
        return mailMessage;
    }

    public static OpenWeatherResponse createOpenWeatherResponse(final OpenWeatherMainDto openWeatherMainDto,
                                                                final OpenWeatherWeatherDto openWeatherWeatherDto) {
        OpenWeatherWeatherDto[] openWeatherWeathers = {openWeatherWeatherDto};
        return OpenWeatherResponse.builder()
                .city("Poznan")
                .openWeatherWeatherDto(openWeatherWeathers)
                .openWeatherMainDto(openWeatherMainDto)
                .build();
    }

    public static AuthenticationResponse createAuthenticationResponse() {
        return AuthenticationResponse.builder()
                .login("login")
                .authenticationToken("authenticationToken")
                .expiresAt(Instant.now().truncatedTo(SECONDS))
                .refreshToken("refreshToken")
                .build();
    }
}
