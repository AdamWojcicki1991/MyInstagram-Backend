package com.myinstagram.scheduler;

import com.myinstagram.domain.entity.User;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.facade.OpenWeatherFacade;
import com.myinstagram.service.MailCreationService;
import com.myinstagram.service.MailSenderService;
import com.myinstagram.service.UserServiceDb;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.myinstagram.util.DtoDataFixture.createOpenWeatherResponseDto;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class EmailSchedulerTestSuite {
    @Spy
    @InjectMocks
    private EmailScheduler emailScheduler;
    @Mock
    private UserServiceDb userServiceDb;
    @Mock
    private OpenWeatherFacade openWeatherFacade;
    @Mock
    private MailSenderService mailSenderService;
    @Mock
    private MailCreationService mailCreationService;

    @Test
    public void shouldSendWeatherEmail() {
        //GIVEN
        User user = createUser("login", "test@gmail.com");
        OpenWeatherResponseDto openWeatherResponseDto = createOpenWeatherResponseDto();
        ResponseEntity<OpenWeatherResponseDto> openWeatherResponseDtoResponseEntity = new ResponseEntity<>(openWeatherResponseDto, OK);
        given(userServiceDb.getAllUsers()).willReturn(List.of(user));
        verify(mailSenderService, times(0)).sendPersonalizedEmail(anyString(), anyString(), anyString());
        given(mailCreationService.createWeatherEmail(user, openWeatherResponseDto)).willReturn("text");
        given(openWeatherFacade.getWeatherResponse(anyString())).willReturn(openWeatherResponseDtoResponseEntity);
        //WHEN
        emailScheduler.sendWeatherEmail();
        //THEN
        verify(emailScheduler, times(1)).sendWeatherEmail();
    }
}
