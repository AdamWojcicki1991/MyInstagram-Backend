package com.myinstagram.service;

import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.mailboxlayer.exceptions.MailBoxLayerApiException;
import com.myinstagram.mailboxlayer.validator.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;

import static com.myinstagram.domain.util.Constants.*;
import static com.myinstagram.util.DataFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
@ExtendWith(MockitoExtension.class)
public class UserServiceTestSuite {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserServiceDb userServiceDb;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private MailSenderService mailSenderService;
    @Mock
    private MailCreationService mailCreationService;
    @Mock
    private PasswordProcessorService passwordProcessorService;

    @Test
    public void shouldUpdateUserProfile() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        UserRequest userRequest = createUserRequest(user);
        User updateUser = updateUser(user, userRequest);
        given(userServiceDb.saveUser(any(User.class))).willReturn(updateUser);
        given(mailCreationService.createUpdateUserProfileEmail(any(User.class))).willReturn("Test String");
        verify(mailSenderService, times(0))
                .sendPersonalizedEmail("test@gmail.com", UPDATE_USER_EMAIL, "Test text");
        //WHEN
        User updateUserProfile = userService.updateUserProfile(user, userRequest);
        //THEN
        assertThat(updateUserProfile).isNotNull();
        assertEquals(updateUser, updateUserProfile);
    }

    @Test
    public void shouldUpdateUserPassword() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        String password = "password";
        User updateUserPassword = updateUserPassword(user, password);
        given(emailValidator.validateUserEmail(anyString())).willReturn(true);
        given(passwordProcessorService.encryptPassword(anyString())).willReturn("encryptedPassword");
        given(userServiceDb.saveUser(any(User.class))).willReturn(updateUserPassword);
        verify(mailSenderService, times(0))
                .sendPersonalizedEmail("email1@gmail.com", UPDATE_USER_PASSWORD_EMAIL, "Test text");
        //WHEN
        User userWithUpdatedPassword = userService.updateUserPassword(user, password);
        //THEN
        assertThat(userWithUpdatedPassword).isNotNull();
        assertEquals(updateUserPassword, userWithUpdatedPassword);
    }

    @Test
    public void shouldThrowValidationExceptionDuringUpdateUserPassword() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        String password = "password";
        given(emailValidator.validateUserEmail(anyString())).willReturn(false);
        //WHEN
        MailBoxLayerApiException mailBoxLayerApiException = assertThrows(
                MailBoxLayerApiException.class,
                () -> userService.updateUserPassword(user, password));
        //THEN
        assertEquals("MailBoxLayerApi error for validation of email: email1@gmail.com", mailBoxLayerApiException.getMessage());
    }

    @Test
    public void shouldResetUserPassword() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        String password = "password";
        User updateUserPassword = updateUserPassword(user, password);
        given(emailValidator.validateUserEmail(anyString())).willReturn(true);
        given(passwordProcessorService.generateUuid()).willReturn("e6eeb33e-3688-11eb-adc1-0242ac120002");
        given(passwordProcessorService.encryptPassword(anyString())).willReturn("encryptedPassword");
        given(userServiceDb.saveUser(any(User.class))).willReturn(updateUserPassword);
        verify(mailSenderService, times(0))
                .sendPersonalizedEmail("email1@gmail.com", RESET_USER_PASSWORD_EMAIL, "Test text");
        //WHEN
        User userWithUpdatedPassword = userService.resetUserPassword(user);
        //THEN
        assertThat(userWithUpdatedPassword).isNotNull();
        assertEquals(updateUserPassword, userWithUpdatedPassword);
    }

    @Test
    public void shouldThrowValidationExceptionDuringResetUserPassword() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        given(emailValidator.validateUserEmail(anyString())).willReturn(false);
        //WHEN
        MailBoxLayerApiException mailBoxLayerApiException = assertThrows(
                MailBoxLayerApiException.class,
                () -> userService.resetUserPassword(user));
        //THEN
        assertEquals("MailBoxLayerApi error for validation of email: email1@gmail.com", mailBoxLayerApiException.getMessage());
    }
}
