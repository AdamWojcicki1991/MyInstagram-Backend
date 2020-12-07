package com.myinstagram.util;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.mail.Mail;
import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import com.myinstagram.openweather.dto.OpenWeatherMainDto;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.dto.OpenWeatherWeatherDto;
import org.springframework.mail.SimpleMailMessage;

import java.time.Instant;

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

    public static SimpleMailMessage getSimpleMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());
        return mailMessage;
    }

    public static ValidateMailResponseDto createValidateResponseDto(final boolean emailFormatValid,
                                                                    final boolean mxRecordsFound,
                                                                    final boolean smtpValid) {
        return ValidateMailResponseDto.builder()
                .validatedEmail("test@gmail.com")
                .emailFormatValid(emailFormatValid)
                .mxRecordsFound(mxRecordsFound)
                .smtpValid(smtpValid)
                .build();
    }


    public static OpenWeatherMainDto createOpenWeatherMainDto() {
        return OpenWeatherMainDto.builder()
                .temperature(12.1)
                .feltTemperature(12.3)
                .humidity(60)
                .pressure(999)
                .build();
    }

    public static OpenWeatherWeatherDto createOpenWeatherWeatherDto() {
        return OpenWeatherWeatherDto.builder()
                .mainWeather("Cloud")
                .weatherDescription("Cloudy weather")
                .build();
    }

    public static OpenWeatherResponseDto createOpenWeatherResponseDto() {
        return OpenWeatherResponseDto.builder()
                .city("Paris")
                .temperature(16)
                .feltTemperature(12.2)
                .pressure(1000)
                .humidity(12)
                .mainWeather("Test Weather")
                .weatherDescription("Test Description")
                .build();
    }

    public static OpenWeatherResponse createOpenWeatherResponse(final OpenWeatherMainDto openWeatherMainDto, final OpenWeatherWeatherDto openWeatherWeatherDto) {
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
                .expiresAt(Instant.now())
                .refreshToken("refreshToken")
                .build();
    }
}
