package com.myinstagram.openweather.client;

import com.myinstagram.openweather.config.OpenWeatherConfig;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class OpenWeatherClient {
    private final RestTemplate restTemplate;
    private final OpenWeatherConfig openWeatherConfig;

    @Async
    public CompletableFuture<OpenWeatherResponse> getWeatherForCityFromUrl(final String city) {
        return CompletableFuture.completedFuture(restTemplate.getForObject(getUri(city),
                                                                           OpenWeatherResponse.class));
    }

    private URI getUri(final String city) {
        return UriComponentsBuilder.fromHttpUrl(openWeatherConfig.getOpenWeatherApiEndpoint())
                .queryParam("q", city)
                .queryParam("appid", openWeatherConfig.getOpenWeatherApiKey())
                .queryParam("units", openWeatherConfig.getOpenWeatherApiUnits())
                .queryParam("lang", openWeatherConfig.getOpenWeatherApiLang())
                .build().encode().toUri();
    }
}
