package com.myinstagram.security.service;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.exceptions.custom.user.UserEnabledException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.service.PasswordProcessorService;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;

import static com.myinstagram.domain.enums.RoleType.NO_ROLE;
import static com.myinstagram.domain.util.Constants.VALID_UUID;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.EntityDataFixture.createVerificationToken;
import static com.myinstagram.util.RequestDataFixture.createRegisterRequest;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class AuthenticationServiceUtilsTestSuite {
    @Autowired
    private AuthenticationServiceUtils authenticationServiceUtils;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private RoleServiceDb roleServiceDb;
    @Autowired
    private PasswordProcessorService passwordProcessorService;
    @Autowired
    private VerificationTokenServiceDb verificationTokenServiceDb;

    @Test
    public void shouldCreateAuthenticationResponse() {
        //GIVEN
        String token = "token";
        String refreshToken = "refreshToken";
        String login = "login";
        Instant expireTime = Instant.now().plusMillis(jwtProvider.jwtExpirationInMillis());
        //WHEN
        AuthenticationResponse authenticationResponse = authenticationServiceUtils.createAuthenticationResponse(token,
                                                                                                                refreshToken,
                                                                                                                login);
        //THEN
        assertEquals(refreshToken, authenticationResponse.getRefreshToken());
        assertEquals(token, authenticationResponse.getAuthenticationToken());
        assertEquals(expireTime.truncatedTo(SECONDS), authenticationResponse.getExpiresAt().truncatedTo(SECONDS));
        assertEquals(login, authenticationResponse.getLogin());
    }

    @Test
    public void shouldGenerateVerificationToken() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login", "test@gmail.com"));
        //WHEN
        String verificationToken = authenticationServiceUtils.generateVerificationToken(user);
        //THEN
        assertEquals(1, verificationTokenServiceDb.getAllVerificationTokens().size());
        assertTrue(verificationToken.matches(VALID_UUID));
    }

    @Test
    public void shouldFetchUserAndEnable() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login", "test@gmail.com").toBuilder().enabled(false).build());
        VerificationToken verificationToken = createVerificationToken(user, "token");
        //WHEN
        boolean isUserFetchAndEnable = authenticationServiceUtils.fetchUserAndEnable(verificationToken);
        //THEN
        User userByLogin = userServiceDb.getUserByLogin("login").get();
        assertTrue(isUserFetchAndEnable);
        assertTrue(userByLogin.isEnabled());
        assertEquals(1, roleServiceDb.getAllRoles().size());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionDuringFetchUserAndEnable() {
        //GIVEN
        User user = createUser("login", "test@gmail.com");
        VerificationToken verificationToken = createVerificationToken(user, "token");
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(
                UserNotFoundException.class,
                () -> authenticationServiceUtils.fetchUserAndEnable(verificationToken));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldThrowUserEnabledExceptionDuringFetchUserAndEnable() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login", "test@gmail.com"));
        VerificationToken verificationToken = createVerificationToken(user, "token");
        //WHEN
        UserEnabledException userEnabledException = assertThrows(
                UserEnabledException.class,
                () -> authenticationServiceUtils.fetchUserAndEnable(verificationToken));
        //THEN
        assertEquals("User is already authorized login: login", userEnabledException.getMessage());
    }

    @Test
    public void shouldAssignUserWithRole() {
        //GIVEN
        RegisterRequest registerRequest = createRegisterRequest("testLogin", "Paris", "test@gmail.com", "testPassword");
        //WHEN
        User userWithAssignedRole = authenticationServiceUtils.assignUserWithRole(registerRequest, NO_ROLE);
        //THEN
        assertEquals(1, userServiceDb.getAllUsers().size());
        assertEquals(registerRequest.getLogin(), userWithAssignedRole.getLogin());
        assertTrue(passwordProcessorService.isEncryptedPasswordMatching(registerRequest.getPassword(),
                                                                        userWithAssignedRole.getPassword()));
        assertEquals(registerRequest.getEmail(), userWithAssignedRole.getEmail());
        assertEquals(registerRequest.getCity(), userWithAssignedRole.getCity());
        assertEquals(1, roleServiceDb.getAllRoles().size());
    }
}
