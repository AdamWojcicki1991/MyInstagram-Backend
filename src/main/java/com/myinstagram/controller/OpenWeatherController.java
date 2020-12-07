package com.myinstagram.controller;

import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.facade.OpenWeatherFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weathers")
public final class OpenWeatherController {
    private final OpenWeatherFacade openWeatherFacade;

    @GetMapping("/{city}")
    public ResponseEntity<OpenWeatherResponseDto> getWeatherForCity(@PathVariable final String city) {
        return openWeatherFacade.getWeatherResponse(city);
    }
}
