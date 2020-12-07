package com.myinstagram.openweather.facade;

import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.mapper.OpenWeatherResponseMapper;
import com.myinstagram.openweather.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenWeatherFacade {
    private final OpenWeatherService openWeatherService;
    private final OpenWeatherResponseMapper openWeatherResponseMapper;

    public ResponseEntity<OpenWeatherResponseDto> getWeatherResponse(final String city) {
        log.info("Weather response correctly retrieved for city: " + city);
        OpenWeatherResponseDto openWeatherResponseDto = openWeatherResponseMapper.mapToOpenWeatherResponseDto(
                openWeatherService.getWeatherForCityResponseFromClient(city));
        return new ResponseEntity<>(openWeatherResponseDto, OK);
    }
}
