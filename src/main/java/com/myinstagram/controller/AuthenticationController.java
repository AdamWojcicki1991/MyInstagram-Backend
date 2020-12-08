package com.myinstagram.controller;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.security.service.AuthenticationService;
import com.myinstagram.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

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

    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable final String token) {
        authenticationService.verifyToken(token);
        return new ResponseEntity<>("Account Activated Successfully!", OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody final RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
        return new ResponseEntity<>("User Register Successfully!", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody final LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshToken(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody final RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!");
    }
}
