package com.myinstagram.facade.user;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.mapper.UserMapper;
import com.myinstagram.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Optional;

import static com.myinstagram.util.DtoDataFixture.createUserDto;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createPasswordRequest;
import static com.myinstagram.util.RequestDataFixture.createUserRequest;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class UserFacadeMockTestSuite {
    @InjectMocks
    private UserFacade userFacade;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;
    @Mock
    private ImageService imageService;
    @Mock
    private UserServiceDb userServiceDb;
    @Mock
    private RoleServiceDb roleServiceDb;
    @Mock
    private UserFacadeUtils userFacadeUtils;
    @Mock
    private VerificationTokenServiceDb verificationTokenServiceDb;

    @Test
    public void shouldUpdateProfile() {
        //GIVEN
        User user = createUser("login", "login@gmail.com").toBuilder().id(1L).build();
        UserRequest userRequest = createUserRequest(user);
        UserDto userDto = createUserDto("login", "test@gmail.com")
                .toBuilder().userName("Test User").description("Test Description").build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        given(userServiceDb.getUserById(userRequest.getUserId())).willReturn(Optional.of(user));
        given(userFacadeUtils.updateProfileIfUserIsValidated(userRequest, user)).willReturn(responseEntity);
        //WHEN
        ResponseEntity<UserDto> userDtoResponseEntity = userFacade.updateProfile(userRequest);
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
    public void shouldChangePassword() {
        //GIVEN
        User user = createUser("login", "login@gmail.com");
        PasswordRequest passwordRequest = createPasswordRequest("password", "newPassword", "newPassword");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Password Changed Successfully!", OK);
        given(userServiceDb.getUserByLogin("login")).willReturn(Optional.of(user));
        given(userFacadeUtils.changePasswordIfUserPasswordIsValidated(passwordRequest, user)).willReturn(responseEntity);
        //WHEN
        ResponseEntity<String> userDtoWithChangedPasswordResponseEntity = userFacade.changePassword(passwordRequest);
        //THEN
        assertEquals(OK, userDtoWithChangedPasswordResponseEntity.getStatusCode());
        assertEquals("Password Changed Successfully!", userDtoWithChangedPasswordResponseEntity.getBody());
    }

    @Test
    public void shouldResetPassword() {
        //GIVEN
        User user = createUser("login", "login@gmail.com").toBuilder().id(1L).build();
        UserRequest userRequest = createUserRequest(user);
        UserDto userDto = createUserDto("login", "test@gmail.com")
                .toBuilder().userName("Test User").description("Test Description").build();
        given(userServiceDb.getUserByEmail("login@gmail.com")).willReturn(Optional.of(user));
        given(userMapper.mapToUserDto(userService.resetUserPassword(user))).willReturn(userDto);
        //WHEN
        ResponseEntity<UserDto> userDtoResponseEntity = userFacade.resetPassword("login@gmail.com");
        //THEN
        assertEquals(OK, userDtoResponseEntity.getStatusCode());
        assertEquals(UserDto.class, userDtoResponseEntity.getBody().getClass());
        assertEquals(userRequest.getUserId(), userDtoResponseEntity.getBody().getId());
        assertEquals(userRequest.getUserName(), userDtoResponseEntity.getBody().getUserName());
        assertEquals(userRequest.getEmail(), userDtoResponseEntity.getBody().getEmail());
        assertEquals(userRequest.getDescription(), userDtoResponseEntity.getBody().getDescription());
        assertEquals(Instant.now().truncatedTo(SECONDS), userDtoResponseEntity.getBody().getUpdateDate().truncatedTo(SECONDS));
    }
}
