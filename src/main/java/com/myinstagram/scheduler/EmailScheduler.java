package com.myinstagram.scheduler;

import com.myinstagram.openweather.facade.OpenWeatherFacade;
import com.myinstagram.service.MailCreationService;
import com.myinstagram.service.MailSenderService;
import com.myinstagram.service.UserServiceDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.myinstagram.domain.util.Constants.WEATHER_EMAIL;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailScheduler {
    private final UserServiceDb userServiceDb;
    private final OpenWeatherFacade openWeatherFacade;
    private final MailSenderService mailSenderService;
    private final MailCreationService mailCreationService;

    @Async
    @Scheduled(cron = "0 0 10 * * *")
    public void sendWeatherEmail() {
        userServiceDb.getAllUsers().forEach(user -> {
            log.info("Sending email to user: " + user.getLogin() + " on email: " + user.getEmail());
            mailSenderService.sendPersonalizedEmail(
                    user.getEmail(),
                    WEATHER_EMAIL,
                    mailCreationService.createWeatherEmail(
                            user,
                            openWeatherFacade.getWeatherResponse(user.getCity()).getBody()));
        });
    }
}
