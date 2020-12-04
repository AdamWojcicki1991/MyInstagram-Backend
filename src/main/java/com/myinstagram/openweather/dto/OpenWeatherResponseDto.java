package com.myinstagram.openweather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OpenWeatherResponseDto {
    @JsonProperty("name")
    private final String city;
    @JsonProperty("temp")
    private final double temperature;
    @JsonProperty("feels_like")
    private final double feltTemperature;
    @JsonProperty("pressure")
    private final int pressure;
    @JsonProperty("humidity")
    private final int humidity;
    @JsonProperty("main")
    private final String mainWeather;
    @JsonProperty("description")
    private final String weatherDescription;
}
