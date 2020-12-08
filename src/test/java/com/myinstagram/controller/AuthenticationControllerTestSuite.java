package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.security.service.AuthenticationService;
import com.myinstagram.security.service.CustomUserDetailsService;
import com.myinstagram.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.myinstagram.util.ControllerDataFixture.*;
import static com.myinstagram.util.DomainDataFixture.createAuthenticationResponse;
import static com.myinstagram.util.EntityDataFixture.*;
import static com.myinstagram.util.RequestDataFixture.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/authentications | GET")
    public void shouldGetRefreshTokens() throws Exception {
        //GIVEN
        RefreshToken firstRefreshToken = createRefreshToken(FIRST_TOKEN).toBuilder().id(1L).build();
        RefreshToken secondRefreshToken = createRefreshToken(SECOND_TOKEN).toBuilder().id(2L).build();
        RefreshToken thirdRefreshToken = createRefreshToken(THIRD_TOKEN).toBuilder().id(3L).build();
        given(refreshTokenService.getRefreshTokens()).willReturn(
                List.of(firstRefreshToken, secondRefreshToken, thirdRefreshToken));
        //WHEN & THEN
        mockMvc.perform(get("/authentications"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].token", is(FIRST_TOKEN)))
                .andExpect(jsonPath("$[0].createDate", is(firstRefreshToken.getCreateDate().toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].token", is(SECOND_TOKEN)))
                .andExpect(jsonPath("$[1].createDate", is(secondRefreshToken.getCreateDate().toString())))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].token", is(THIRD_TOKEN)))
                .andExpect(jsonPath("$[2].createDate", is(thirdRefreshToken.getCreateDate().toString())));
    }

    @Test
    @DisplayName("/authentications | GET")
    public void shouldGetEmptyRefreshTokens() throws Exception {
        //GIVEN
        //WHEN & THEN
        mockMvc.perform(get("/authentications"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/authentications/verify/{token} | GET")
    public void shouldVerifyAccount() throws Exception {
        //GIVEN
        VerificationToken verificationToken = createVerificationToken(createUser("login_1", "test1@gmail.com"), FIRST_TOKEN);
        given(authenticationService.verifyToken(verificationToken.getToken())).willReturn(true);
        //WHEN & THEN
        mockMvc.perform(get("/authentications/verify/e6eeb33e-3688-11eb-adc1-0242ac120002"))
                .andExpect(status().is(200))
                .andExpect(content().string("Account Activated Successfully!"));
    }

    @Test
    @DisplayName("/authentications/signup | POST")
    public void shouldSignup() throws Exception {
        //GIVEN
        RegisterRequest registerRequest = createRegisterRequest("Name", "login", "Poznan", "test@gmail.com", "Password");
        String jsonContent = gson.toJson(registerRequest);
        given(authenticationService.register(registerRequest)).willReturn(true);
        //WHEN & THEN
        mockMvc.perform(post("/authentications/signup")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(content().string("User Register Successfully!"));
    }

    @Test
    @DisplayName("/authentications/login | POST")
    public void shouldLogin() throws Exception {
        //GIVEN
        LoginRequest loginRequest = createLoginRequest();
        AuthenticationResponse authenticationResponse = createAuthenticationResponse();
        String jsonContent = gson.toJson(loginRequest);
        given(authenticationService.login(loginRequest)).willReturn(authenticationResponse);
        //WHEN & THEN
        mockMvc.perform(post("/authentications/login")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.authenticationToken", is(authenticationResponse.getAuthenticationToken())))
                .andExpect(jsonPath("$.login", is(authenticationResponse.getLogin())))
                .andExpect(jsonPath("$.refreshToken", is(authenticationResponse.getRefreshToken())))
                .andExpect(jsonPath("$.expiresAt", is(authenticationResponse.getExpiresAt().toString())));
    }

    @Test
    @DisplayName("/authentications/refresh | POST")
    public void shouldRefreshToken() throws Exception {
        //GIVEN
        RefreshTokenRequest refreshTokenRequest = createRefreshTokenRequest();
        AuthenticationResponse authenticationResponse = createAuthenticationResponse();
        String jsonContent = gson.toJson(refreshTokenRequest);
        given(authenticationService.refreshToken(refreshTokenRequest)).willReturn(authenticationResponse);
        //WHEN & THEN
        mockMvc.perform(post("/authentications/refresh")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.authenticationToken", is(authenticationResponse.getAuthenticationToken())))
                .andExpect(jsonPath("$.login", is(authenticationResponse.getLogin())))
                .andExpect(jsonPath("$.refreshToken", is(authenticationResponse.getRefreshToken())))
                .andExpect(jsonPath("$.expiresAt", is(authenticationResponse.getExpiresAt().toString())));
    }

    @Test
    @DisplayName("/authentications/logout | DELETE")
    public void shouldLogout() throws Exception {
        //GIVEN
        RefreshTokenRequest refreshTokenRequest = createRefreshTokenRequest();
        String jsonContent = gson.toJson(refreshTokenRequest);
        verify(refreshTokenService, times(0)).deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        //WHEN & THEN
        mockMvc.perform(delete("/authentications/logout")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(content().string("Refresh Token Deleted Successfully!"));
    }
}
