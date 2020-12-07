package com.myinstagram.openweather.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OpenWeatherConfig {
    @Value("${openweather.api.endpoint.prod}")
    private String openWeatherApiEndpoint;
    @Value("${openweather.api.key}")
    private String openWeatherApiKey;
    @Value("${openweather.api.units}")
    private String openWeatherApiUnits;
    @Value("${openweather.api.lang}")
    private String openWeatherApiLang;
}
