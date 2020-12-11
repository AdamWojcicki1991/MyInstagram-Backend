package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import com.myinstagram.mailboxlayer.facade.MailBoxLayerFacade;
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

import static com.myinstagram.util.DtoDataFixture.createValidateResponseDto;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "user", password = "password")
@WebMvcTest(MailBoxLayerController.class)
public class MailBoxLayerControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private MailBoxLayerFacade mailBoxLayerFacade;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/validates/{email} | GET")
    public void shouldGetValidationEmail() throws Exception {
        //GIVEN
        ValidateMailResponseDto validateResponseDto = createValidateResponseDto(true, true, true);
        ResponseEntity<ValidateMailResponseDto> responseEntity = new ResponseEntity<>(validateResponseDto, OK);
        given(mailBoxLayerFacade.getValidateMailResponse("test@gmail.com")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/validates/test@gmail.com"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.email", is("test@gmail.com")))
                .andExpect(jsonPath("$.format_valid", is(true)))
                .andExpect(jsonPath("$.mx_found", is(true)))
                .andExpect(jsonPath("$.smtp_check", is(true)));
    }
}
