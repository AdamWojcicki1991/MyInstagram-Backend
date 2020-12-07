package com.myinstagram.openweather.service;

import com.myinstagram.openweather.client.OpenWeatherClient;
import com.myinstagram.openweather.dto.OpenWeatherMainDto;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherWeatherDto;
import com.myinstagram.openweather.exceptions.OpenWeatherApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.myinstagram.util.DataFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherServiceTestSuite {
    @InjectMocks
    private OpenWeatherService openWeatherService;
    @Mock
    private OpenWeatherClient openWeatherClient;

    @Test
    public void shouldGetWeatherForCityResponseFromClient() {
        //GIVEN
        String city = "Poznan";
        OpenWeatherWeatherDto openWeatherWeatherDto = createOpenWeatherWeatherDto();
        OpenWeatherMainDto openWeatherMainDto = createOpenWeatherMainDto();
        OpenWeatherResponse openWeatherResponse = createOpenWeatherResponse(openWeatherMainDto, openWeatherWeatherDto);
        CompletableFuture<OpenWeatherResponse> completableFuture = CompletableFuture.completedFuture(openWeatherResponse);
        given(openWeatherClient.getWeatherForCityFromUrl(city)).willReturn(completableFuture);
        //WHEN
        OpenWeatherResponse weatherForCityResponseFromClient = openWeatherService.getWeatherForCityResponseFromClient("Poznan");
        //THEN
        assertEquals(openWeatherResponse, weatherForCityResponseFromClient);
    }

    @Test
    public void shouldNotGetWeatherForCityResponseFromClientAndThrowOpenWeatherApiException() throws ExecutionException, InterruptedException {
        //GIVEN
        String city = "Poznan";
        given(openWeatherClient.getWeatherForCityFromUrl(city)).willThrow(OpenWeatherApiException.class);
        //WHEN && THEN
        assertThatThrownBy(() -> openWeatherService.getWeatherForCityResponseFromClient(city))
                .isInstanceOf(OpenWeatherApiException.class)
                .hasMessage("OpenWeatherApi error for receiving createWeatherEmail data from city: Poznan");
    }
}
