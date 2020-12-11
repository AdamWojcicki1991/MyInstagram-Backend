package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.facade.OpenWeatherFacade;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.security.service.AuthenticationService;
import com.myinstagram.security.service.CustomUserDetailsService;
import com.myinstagram.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.myinstagram.util.DtoDataFixture.createOpenWeatherResponseDto;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "user", password = "password")
@WebMvcTest(OpenWeatherController.class)
public class OpenWeatherControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private OpenWeatherFacade openWeatherFacade;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/weathers/{city} | GET")
    public void shouldGetWeatherForCity() throws Exception {
        //GIVEN
        OpenWeatherResponseDto openWeatherResponseDto = createOpenWeatherResponseDto();
        ResponseEntity<OpenWeatherResponseDto> responseEntity = new ResponseEntity<>(openWeatherResponseDto, OK);
        given(openWeatherFacade.getWeatherResponse("Paris")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/weathers/Paris"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name", is("Paris")))
                .andExpect(jsonPath("$.temp", is(16.0)))
                .andExpect(jsonPath("$.feels_like", is(12.2)))
                .andExpect(jsonPath("$.pressure", is(1000)))
                .andExpect(jsonPath("$.humidity", is(12)))
                .andExpect(jsonPath("$.main", is("Test Weather")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }
}
