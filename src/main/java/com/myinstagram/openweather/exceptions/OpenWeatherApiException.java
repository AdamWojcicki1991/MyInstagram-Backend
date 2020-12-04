package com.myinstagram.openweather.exceptions;

public class OpenWeatherApiException extends RuntimeException {
    public OpenWeatherApiException(String city) {
        super("OpenWeatherApi error for receiving createWeatherEmail data from city: " + city);
    }
}
