package com.myinstagram.openweather.service;

import com.myinstagram.openweather.client.OpenWeatherClient;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.exceptions.OpenWeatherApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenWeatherService {
    private final OpenWeatherClient openWeatherClient;

    public OpenWeatherResponse getWeatherForCityResponseFromClient(final String city) {
        log.info("Weather response from OpenWeather API sent for city: " + city);
        try {
            return openWeatherClient.getWeatherForCityFromUrl(city).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new OpenWeatherApiException(city);
        }
    }
}
