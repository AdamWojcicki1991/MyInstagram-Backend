package com.myinstagram.security.service;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.domain.enums.RoleType;
import com.myinstagram.exceptions.custom.user.UserEnabledException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.service.PasswordProcessorService;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.myinstagram.domain.enums.RoleType.USER;
import static com.myinstagram.domain.enums.UserStatus.ACTIVE;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
class AuthenticationServiceUtils {
    private final JwtProvider jwtProvider;
    private final UserServiceDb userServiceDb;
    private final RoleServiceDb roleServiceDb;
    private final PasswordProcessorService passwordProcessorService;
    private final VerificationTokenServiceDb verificationTokenServiceDb;

    AuthenticationResponse createAuthenticationResponse(final String token, final String refreshToken, final String login) {
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshToken)
                .expiresAt(Instant.now().plusMillis(jwtProvider.jwtExpirationInMillis()))
                .login(login)
                .build();
    }

    String generateVerificationToken(final User user) {
        String token = passwordProcessorService.generateUuid();
        verificationTokenServiceDb.saveVerificationToken(VerificationToken.builder()
                                                                 .token(token)
                                                                 .user(user)
                                                                 .expirationDate(Instant.now().plusMillis(
                                                                         jwtProvider.jwtExpirationInMillis()))
                                                                 .build());
        return token;
    }

    void fetchUserAndEnable(final VerificationToken verificationToken) {
        String login = verificationToken.getUser().getLogin();
        User user = userServiceDb.getUserByLogin(login).orElseThrow(() ->
                                                                            new UserNotFoundException(login));
        if (!user.isEnabled()) {
            user.toBuilder().enabled(true).build();
            assignRole(user, USER);
            userServiceDb.saveUser(user);
            log.info("User authenticated correctly!");
        } else {
            throw new UserEnabledException(user.getLogin());
        }
    }

    User assignUserWithRole(final RegisterRequest registerRequest, final RoleType roleType) {
        User user = assignUser(registerRequest);
        assignRole(user, roleType);
        return user;
    }

    private User assignUser(final RegisterRequest registerRequest) {
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

    private void assignRole(final User user, final RoleType roleType) {
        roleServiceDb.saveRole(Role.builder()
                                       .roleType(roleType)
                                       .users(new HashSet<>(Set.of(user)))
                                       .build());
    }
}
