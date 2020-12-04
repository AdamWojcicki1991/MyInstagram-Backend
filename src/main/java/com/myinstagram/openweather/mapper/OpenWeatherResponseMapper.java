package com.myinstagram.openweather.mapper;

import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpenWeatherResponseMapper {
    @Mapping(target = "city", expression = "java(openWeatherResponse.getCity())")
    @Mapping(target = "temperature", expression = "java(openWeatherResponse.getOpenWeatherMainDto().getTemperature())")
    @Mapping(target = "feltTemperature", expression = "java(openWeatherResponse.getOpenWeatherMainDto().getFeltTemperature())")
    @Mapping(target = "pressure", expression = "java(openWeatherResponse.getOpenWeatherMainDto().getPressure())")
    @Mapping(target = "humidity", expression = "java(openWeatherResponse.getOpenWeatherMainDto().getHumidity())")
    @Mapping(target = "mainWeather", expression = "java(openWeatherResponse.getOpenWeatherWeatherDto()[0].getMainWeather())")
    @Mapping(target = "weatherDescription", expression = "java(openWeatherResponse.getOpenWeatherWeatherDto()[0].getWeatherDescription())")
    OpenWeatherResponseDto mapToOpenWeatherResponseDto(OpenWeatherResponse openWeatherResponse);
}
