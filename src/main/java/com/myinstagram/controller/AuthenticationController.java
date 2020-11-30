package com.myinstagram.controller;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.security.AuthenticationService;
import com.myinstagram.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/authentications")
public final class AuthenticationController {
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<RefreshToken>> getRefreshTokens() {
        List<RefreshToken> refreshTokens = refreshTokenService.getRefreshTokens();
        return new ResponseEntity<>(refreshTokens, OK);
    }

    @PostMapping(value = "/signup", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody final RegisterRequest registerRequest) {
        authenticationService.signup(registerRequest);
        return new ResponseEntity<>("User Registrar Successfully!", OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable final String token) {
        authenticationService.verifyToken(token);
        return new ResponseEntity<>("Account Activated Successfully!", OK);
    }

    @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
    public AuthenticationResponse login(@RequestBody final LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping(value = "/refresh", consumes = APPLICATION_JSON_VALUE)
    public AuthenticationResponse refreshToken(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!!");
    }
}
