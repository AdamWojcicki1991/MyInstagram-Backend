package com.myinstagram.openweather.facade;

import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.mapper.OpenWeatherResponseMapper;
import com.myinstagram.openweather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenWeatherFacade {
    private final OpenWeatherService openWeatherService;
    private final OpenWeatherResponseMapper openWeatherResponseMapper;

    public OpenWeatherResponseDto getWeatherResponse(final String city) {
        log.info("Weather response correctly retrieved for city: " + city);
        return openWeatherResponseMapper.mapToOpenWeatherResponseDto(
                openWeatherService.getWeatherForCityResponseFromClient(city));
    }
}
