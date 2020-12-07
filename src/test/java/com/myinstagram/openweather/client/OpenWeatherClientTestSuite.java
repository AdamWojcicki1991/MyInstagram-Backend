package com.myinstagram.openweather.client;

import com.myinstagram.openweather.config.OpenWeatherConfig;
import com.myinstagram.openweather.dto.OpenWeatherMainDto;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherWeatherDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.myinstagram.util.DomainDataFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherClientTestSuite {
    @InjectMocks
    private OpenWeatherClient openWeatherClient;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private OpenWeatherConfig openWeatherConfig;

    @BeforeEach
    public void setUp() {
        given(openWeatherConfig.getOpenWeatherApiEndpoint()).willReturn("http://test.org/weather");
        given(openWeatherConfig.getOpenWeatherApiKey()).willReturn("test");
        given(openWeatherConfig.getOpenWeatherApiUnits()).willReturn("test");
        given(openWeatherConfig.getOpenWeatherApiLang()).willReturn("test");
    }

    @Test
    public void shouldGetWeatherForCityFromUrl() throws URISyntaxException, ExecutionException, InterruptedException {
        //GIVEN
        OpenWeatherMainDto openWeatherMainDto = createOpenWeatherMainDto();
        OpenWeatherWeatherDto openWeatherWeatherDto = createOpenWeatherWeatherDto();
        OpenWeatherResponse openWeatherResponse = createOpenWeatherResponse(openWeatherMainDto, openWeatherWeatherDto);
        URI uri = new URI("http://test.org/weather" + "?q=Poznan&appid=test&units=test&lang=test");
        given(restTemplate.getForObject(uri, OpenWeatherResponse.class)).willReturn(openWeatherResponse);
        //WHEN
        CompletableFuture<OpenWeatherResponse> weatherForCityFromUrl = openWeatherClient.getWeatherForCityFromUrl("Poznan");
        //THEN
        assertEquals("Poznan", weatherForCityFromUrl.get().getCity());
        assertEquals(openWeatherResponse.getOpenWeatherWeatherDto(), weatherForCityFromUrl.get().getOpenWeatherWeatherDto());
        assertEquals(openWeatherMainDto, weatherForCityFromUrl.get().getOpenWeatherMainDto());
    }
}
