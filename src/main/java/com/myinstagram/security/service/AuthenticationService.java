package com.myinstagram.security.service;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.custom.security.VerificationTokenNotFoundException;
import com.myinstagram.exceptions.custom.user.UserFoundException;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.myinstagram.domain.enums.RoleType.NO_ROLE;
import static com.myinstagram.domain.util.Constants.NEW_USER_EMAIL;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthenticationService {
    private final JwtProvider jwtProvider;
    private final ImageService imageService;
    private final UserServiceDb userServiceDb;
    private final MailSenderService mailSenderService;
    private final RefreshTokenService refreshTokenService;
    private final MailCreationService mailCreationService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenServiceDb verificationTokenServiceDb;
    private final AuthenticationServiceUtils authenticationServiceUtils;

    public void signup(final RegisterRequest registerRequest) {
        List<User> users = userServiceDb.getAllUsersByLoginContaining(registerRequest.getLogin());
        if (users.isEmpty()) {
            User user = authenticationServiceUtils.assignUserWithRole(registerRequest, NO_ROLE);
            String token = authenticationServiceUtils.generateVerificationToken(user);
            imageService.loadDefaultUserImage(user);
            mailSenderService.sendPersonalizedEmail(user.getEmail(), NEW_USER_EMAIL,
                                                    mailCreationService.createNewUserEmail(user, token));
        } else {
            throw new UserFoundException(registerRequest.getLogin());
        }
    }

    public void verifyToken(final String token) {
        VerificationToken verificationToken = verificationTokenServiceDb.getVerificationTokenByToken(token)
                .orElseThrow(() -> new VerificationTokenNotFoundException(token));
        authenticationServiceUtils.fetchUserAndEnable(verificationToken);
    }

    public AuthenticationResponse login(final LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return authenticationServiceUtils.createAuthenticationResponse(
                token,
                refreshTokenService.generateRefreshToken().getToken(),
                loginRequest.getLogin());
    }

    public AuthenticationResponse refreshToken(final RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithLogin(refreshTokenRequest.getLogin());
        return authenticationServiceUtils.createAuthenticationResponse(
                token,
                refreshTokenRequest.getRefreshToken(),
                refreshTokenRequest.getLogin());
    }
}
