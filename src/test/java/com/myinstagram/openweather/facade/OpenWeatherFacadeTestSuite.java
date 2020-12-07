package com.myinstagram.openweather.facade;

import com.myinstagram.openweather.dto.OpenWeatherMainDto;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.dto.OpenWeatherWeatherDto;
import com.myinstagram.openweather.mapper.OpenWeatherResponseMapper;
import com.myinstagram.openweather.service.OpenWeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.myinstagram.util.DomainDataFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherFacadeTestSuite {
    @InjectMocks
    private OpenWeatherFacade openWeatherFacade;
    @Mock
    private OpenWeatherService openWeatherService;
    @Mock
    private OpenWeatherResponseMapper openWeatherResponseMapper;

    @Test
    public void shouldGetWeatherResponse() {
        //GIVEN
        String city = " Poznan";
        OpenWeatherMainDto openWeatherMainDto = createOpenWeatherMainDto();
        OpenWeatherWeatherDto openWeatherWeatherDto = createOpenWeatherWeatherDto();
        OpenWeatherResponse openWeatherResponse = createOpenWeatherResponse(openWeatherMainDto, openWeatherWeatherDto);
        OpenWeatherResponseDto openWeatherResponseDto = createOpenWeatherResponseDto();
        ResponseEntity<OpenWeatherResponseDto> openWeatherResponseDtoResponseEntity = new ResponseEntity<>(openWeatherResponseDto, OK);
        given(openWeatherResponseMapper.mapToOpenWeatherResponseDto(openWeatherResponse)).willReturn(openWeatherResponseDto);
        given(openWeatherService.getWeatherForCityResponseFromClient(city)).willReturn(openWeatherResponse);
        //WHEN
        ResponseEntity<OpenWeatherResponseDto> weatherResponse = openWeatherFacade.getWeatherResponse(city);
        //THEN
        assertEquals(openWeatherResponseDtoResponseEntity, weatherResponse);
    }
}
