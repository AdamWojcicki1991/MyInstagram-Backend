package com.myinstagram.security.service;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.custom.register.RegisterRequestException;
import com.myinstagram.exceptions.custom.security.VerificationTokenNotFoundException;
import com.myinstagram.exceptions.custom.user.UserRegistrationException;
import com.myinstagram.mailboxlayer.validator.EmailValidator;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.service.*;
import com.myinstagram.validator.ValidateRegisterRequest;
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
    private final EmailValidator emailValidator;
    private final MailSenderService mailSenderService;
    private final RefreshTokenService refreshTokenService;
    private final MailCreationService mailCreationService;
    private final AuthenticationManager authenticationManager;
    private final ValidateRegisterRequest validateRegisterRequest;
    private final VerificationTokenServiceDb verificationTokenServiceDb;
    private final AuthenticationServiceUtils authenticationServiceUtils;

    public void register(final RegisterRequest registerRequest) {
        boolean isRegisterRequestValid = validateRegisterRequest.isRegisterRequestValid(registerRequest);
        List<User> users = userServiceDb.getAllUsersByLoginContaining(registerRequest.getLogin());
        if (isRegisterRequestValid && users.isEmpty() && (emailValidator.validateUserEmail(registerRequest.getEmail()))) {
            User user = authenticationServiceUtils.assignUserWithRole(registerRequest, NO_ROLE);
            String token = authenticationServiceUtils.generateVerificationToken(user);
            imageService.loadDefaultUserImage(user);
            mailSenderService.sendPersonalizedEmail(user.getEmail(), NEW_USER_EMAIL,
                                                    mailCreationService.createNewUserEmail(user, token));
        } else {
            throwCustomExceptions(registerRequest, isRegisterRequestValid);
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

    private void throwCustomExceptions(final RegisterRequest registerRequest, final boolean isRegisterRequestValid) {
        if (!isRegisterRequestValid) {
            throw new RegisterRequestException(registerRequest);
        } else {
            throw new UserRegistrationException(registerRequest.getLogin(), registerRequest.getEmail());
        }
    }
}
