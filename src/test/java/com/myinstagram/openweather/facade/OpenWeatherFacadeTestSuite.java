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

import static com.myinstagram.util.DataFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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
        given(openWeatherResponseMapper.mapToOpenWeatherResponseDto(openWeatherResponse)).willReturn(openWeatherResponseDto);
        given(openWeatherService.getWeatherForCityResponseFromClient(city)).willReturn(openWeatherResponse);
        //WHEN
        OpenWeatherResponseDto weatherResponse = openWeatherFacade.getWeatherResponse(city);
        //THEN
        assertEquals(openWeatherResponseDto, weatherResponse);
    }
}
