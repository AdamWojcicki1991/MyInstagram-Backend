package com.myinstagram.security;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.custom.UserFoundException;
import com.myinstagram.exceptions.custom.UserNotFoundException;
import com.myinstagram.exceptions.custom.VerificationTokenNotFoundException;
import com.myinstagram.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;
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
    private final PasswordProcessorService passwordProcessorService;
    private final VerificationTokenServiceDb verificationTokenServiceDb;

    public void signup(final RegisterRequest registerRequest) {
        List<User> users = userServiceDb.getAllUsersByLoginContaining(registerRequest.getLogin());
        if (users.isEmpty()){
        User user = createUser(registerRequest);
        String token = generateVerificationToken(user);
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
        fetchUserAndEnable(verificationToken);
    }

    public AuthenticationResponse login(final LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.jwtExpirationInMillis()))
                .login(loginRequest.getLogin())
                .build();
    }

    public AuthenticationResponse refreshToken(final RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithLogin(refreshTokenRequest.getLogin());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.jwtExpirationInMillis()))
                .login(refreshTokenRequest.getLogin())
                .build();
    }

    private String generateVerificationToken(final User user) {
        String token = passwordProcessorService.generateUuid();
        verificationTokenServiceDb.saveVerificationToken(VerificationToken.builder()
                                                                 .token(token)
                                                                 .user(user)
                                                                 .expirationDate(Instant.now().plusMillis(
                                                                         jwtProvider.jwtExpirationInMillis()))
                                                                 .build());
        return token;
    }

    private void fetchUserAndEnable(final VerificationToken verificationToken) {
        String login = verificationToken.getUser().getLogin();
        User user = userServiceDb.getUserByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
        userServiceDb.saveUser(user.toBuilder().enabled(true).build());
    }

    private User createUser(final RegisterRequest registerRequest) {
        return userServiceDb.saveUser(User.builder()
                                              .userName(registerRequest.getName())
                                              .login(registerRequest.getLogin())
                                              .password(passwordProcessorService.encryptPassword(registerRequest.getPassword()))
                                              .email(registerRequest.getEmail())
                                              .description("")
                                              .createDate(LocalDate.now())
                                              .userStatus(ACTIVE)
                                              .enabled(false)
                                              .posts(new ArrayList<>())
                                              .roles(new HashSet<>())
                                              .build());
    }
}
