package com.myinstagram.facade.user;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.security.ChangePasswordException;
import com.myinstagram.exceptions.custom.security.PasswordNotMatchedException;
import com.myinstagram.exceptions.custom.user.UserValidationException;
import com.myinstagram.mapper.UserMapper;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserService;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import com.myinstagram.validator.PasswordValidator;
import com.myinstagram.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static com.myinstagram.util.DtoDataFixture.createUserDto;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createPasswordRequest;
import static com.myinstagram.util.RequestDataFixture.createUserRequest;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class UserFacadeUtilsMockTestSuite {
    @InjectMocks
    private UserFacadeUtils userFacadeUtils;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;
    @Mock
    private UserServiceDb userServiceDb;
    @Mock
    private RoleServiceDb roleServiceDb;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PasswordValidator passwordValidator;
    @Mock
    private VerificationTokenServiceDb verificationTokenServiceDb;

    @Test
    public void shouldUpdateProfileIfUserIsValidated() {
        //GIVEN
        User user = createUser("login", "test@gmail.com").toBuilder().id(1L).build();
        UserRequest userRequest = createUserRequest(user);
        UserDto userDto = createUserDto("login", "test@gmail.com")
                .toBuilder().userName("Test User").description("Test Description").build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        given(userValidator.isUserValidateToAssignEmail(user, userRequest)).willReturn(true);
        given(userMapper.mapToUserDto(userService.updateUserProfile(user, userRequest))).willReturn(userDto);
        //WHEN
        ResponseEntity<UserDto> userDtoResponseEntity = userFacadeUtils.updateProfileIfUserIsValidated(userRequest, user);
        //THEN
        assertEquals(OK, userDtoResponseEntity.getStatusCode());
        assertEquals(UserDto.class, userDtoResponseEntity.getBody().getClass());
        assertEquals(userRequest.getUserId(), userDtoResponseEntity.getBody().getId());
        assertEquals(userRequest.getUserName(), userDtoResponseEntity.getBody().getUserName());
        assertEquals(userRequest.getEmail(), userDtoResponseEntity.getBody().getEmail());
        assertEquals(userRequest.getDescription(), userDtoResponseEntity.getBody().getDescription());
        assertEquals(Instant.now().truncatedTo(SECONDS), userDtoResponseEntity.getBody().getUpdateDate().truncatedTo(SECONDS));
    }

    @Test
    public void shouldNotUpdateProfileIfUserIsNotValidatedAndThrowUserValidationException() {
        //GIVEN
        User user = createUser("login", "test@gmail.com").toBuilder().id(1L).build();
        UserRequest userRequest = createUserRequest(user);
        given(userValidator.isUserValidateToAssignEmail(user, userRequest)).willReturn(false);
        //WHEN & THEN
        assertThatThrownBy(() -> userFacadeUtils.updateProfileIfUserIsValidated(userRequest, user))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("User login is not authorized or new email is already occupied!");
    }

    @Test
    public void shouldChangePasswordIfUserPasswordIsValidated() {
        //GIVEN
        User user = createUser("login", "test@gmail.com").toBuilder().id(1L).build();
        User userWithUpdatedPassword = user.toBuilder().password("newPassword").build();
        PasswordRequest passwordRequest = createPasswordRequest("Password", "newPassword", "newPassword");
        given(passwordValidator.validateNewPasswordWithConfirmed(passwordRequest)).willReturn(false);
        given(passwordValidator.validatePasswords(user, passwordRequest)).willReturn(true);
        given(userService.updateUserPassword(user, passwordRequest.getNewPassword())).willReturn(userWithUpdatedPassword);
        //WHEN
        ResponseEntity<String> stringResponseEntity = userFacadeUtils.changePasswordIfUserPasswordIsValidated(passwordRequest, user);
        //THEN
        assertEquals(OK, stringResponseEntity.getStatusCode());
        assertEquals("Password Changed Successfully!", stringResponseEntity.getBody());
    }

    @Test
    public void shouldNotChangePasswordIfUserPasswordIsNotValidatedAndThrowPasswordNotMatchedException() {
        //GIVEN
        User user = createUser("login", "test@gmail.com").toBuilder().id(1L).build();
        PasswordRequest passwordRequest = createPasswordRequest("Password", "newPassword", "Password");
        given(passwordValidator.validateNewPasswordWithConfirmed(passwordRequest)).willReturn(true);
        //WHEN & THEN
        assertThatThrownBy(() -> userFacadeUtils.changePasswordIfUserPasswordIsValidated(passwordRequest, user))
                .isInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("New password and confirmed password not matched!");
    }

    @Test
    public void shouldNotChangePasswordIfUserPasswordsIsNotValidatedAndReturnBadRequest() {
        //GIVEN
        User user = createUser("login", "test@gmail.com").toBuilder().id(1L).build();
        PasswordRequest passwordRequest = createPasswordRequest("Password", "newPassword", "newPassword");
        given(passwordValidator.validateNewPasswordWithConfirmed(passwordRequest)).willReturn(false);
        given(passwordValidator.validatePasswords(user, passwordRequest)).willReturn(false);
        //WHEN
        ResponseEntity<String> stringResponseEntity = userFacadeUtils.changePasswordIfUserPasswordIsValidated(passwordRequest, user);
        //THEN
        assertEquals(BAD_REQUEST, stringResponseEntity.getStatusCode());
        assertEquals("Current Password is Incorrect", stringResponseEntity.getBody());
    }

    @Test
    public void shouldNotChangePasswordIfUserPasswordIsNotValidatedAndThrowChangePasswordException() {
        //GIVEN
        User user = createUser("login", "test@gmail.com").toBuilder().id(1L).build();
        PasswordRequest passwordRequest = createPasswordRequest("Password", "newPassword", "newPassword");
        given(passwordValidator.validateNewPasswordWithConfirmed(passwordRequest)).willReturn(false);
        given(passwordValidator.validatePasswords(user, passwordRequest)).willReturn(true);
        given(userService.updateUserPassword(user, passwordRequest.getNewPassword())).willThrow(ChangePasswordException.class);
        //WHEN & THEN
        assertThatThrownBy(() -> userFacadeUtils.changePasswordIfUserPasswordIsValidated(passwordRequest, user))
                .isInstanceOf(ChangePasswordException.class)
                .hasMessage("Error occurred during changing password process!");
    }
}
