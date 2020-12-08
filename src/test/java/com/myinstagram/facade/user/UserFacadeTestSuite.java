package com.myinstagram.facade.user;

import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.user.UserNotFoundByIdException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.service.UserServiceDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

import static com.myinstagram.domain.util.Constants.PICTURE_SAVED_TO_SERVER;
import static com.myinstagram.domain.util.Constants.USER_FOLDER;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createPasswordRequest;
import static com.myinstagram.util.RequestDataFixture.createUserRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.OK;

@Transactional
@SpringBootTest
public class UserFacadeTestSuite {
    @Autowired
    private UserFacade userFacade;
    @Autowired
    private UserServiceDb userServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        userServiceDb.saveUser(createUser("login_1", "email1@gmail.com"));
        userServiceDb.saveUser(createUser("next_login_2", "email2@gmail.com"));
        userServiceDb.saveUser(createUser("logging_3", "email3@gmail.com"));
    }

    @Test
    public void shouldGetUsers() {
        //GIVEN
        List<User> users = userServiceDb.getAllUsers();
        //WHEN
        ResponseEntity<List<UserDto>> usersDtoResponseEntity = userFacade.getUsers();
        //THEN
        assertEquals(3, usersDtoResponseEntity.getBody().size());
        assertEquals(OK, usersDtoResponseEntity.getStatusCode());
        IntStream.range(0, 3).forEach(integer -> {
            assertEquals(UserDto.class, usersDtoResponseEntity.getBody().get(integer).getClass());
            assertEquals(users.get(integer).getId(), usersDtoResponseEntity.getBody().get(integer).getId());
        });
    }

    @Test
    public void shouldGetUsersByLogin() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        //WHEN
        ResponseEntity<List<UserDto>> usersDtoResponseEntity = userFacade.getUsersByLogin(user.getLogin());
        //THEN
        assertEquals(1, usersDtoResponseEntity.getBody().size());
        assertEquals(OK, usersDtoResponseEntity.getStatusCode());
        assertEquals(UserDto.class, usersDtoResponseEntity.getBody().get(0).getClass());
        assertEquals(user.getId(), usersDtoResponseEntity.getBody().get(0).getId());
    }

    @Test
    public void shouldGetUserById() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        //WHEN
        ResponseEntity<UserDto> userDtoResponseEntity = userFacade.getUserById(user.getId());
        //THEN
        assertEquals(OK, userDtoResponseEntity.getStatusCode());
        assertEquals(UserDto.class, userDtoResponseEntity.getBody().getClass());
        assertEquals(user.getId(), userDtoResponseEntity.getBody().getId());
    }

    @Test
    public void shouldNotGetUserByIdAndThrowUserNotFoundByIdException() {
        //WHEN
        UserNotFoundByIdException userNotFoundByIdException = assertThrows(UserNotFoundByIdException.class,
                                                                           () -> userFacade.getUserById(123L));
        //THEN
        assertEquals("Could not find user by id: 123", userNotFoundByIdException.getMessage());
    }

    @Test
    public void shouldGetUserByLogin() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        //WHEN
        ResponseEntity<UserDto> userDtoResponseEntity = userFacade.getUserByLogin(user.getLogin());
        //THEN
        assertEquals(OK, userDtoResponseEntity.getStatusCode());
        assertEquals(UserDto.class, userDtoResponseEntity.getBody().getClass());
        assertEquals(user.getId(), userDtoResponseEntity.getBody().getId());
    }

    @Test
    public void shouldNotGetUserByLoginAndThrowUserNotFoundException() {
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> userFacade.getUserByLogin("login"));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldGetUserByMail() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        //WHEN
        ResponseEntity<UserDto> userDtoResponseEntity = userFacade.getUserByMail(user.getEmail());
        //THEN
        assertEquals(OK, userDtoResponseEntity.getStatusCode());
        assertEquals(UserDto.class, userDtoResponseEntity.getBody().getClass());
        assertEquals(user.getId(), userDtoResponseEntity.getBody().getId());
    }

    @Test
    public void shouldNotGetUserByMailAndThrowUserNotFoundException() {
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> userFacade.getUserByMail("test@gmail.com"));
        //THEN
        assertEquals("Could not find user by: test@gmail.com", userNotFoundException.getMessage());
    }

    @Test
    public void shouldNotUpdateProfileAndThrowUserNotFoundByIdException() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        UserRequest userRequest = createUserRequest(user).toBuilder().userId(123L).build();
        //WHEN
        UserNotFoundByIdException userNotFoundByIdException = assertThrows(UserNotFoundByIdException.class,
                                                                           () -> userFacade.updateProfile(userRequest));
        //THEN
        assertEquals("Could not find user by id: 123", userNotFoundByIdException.getMessage());
    }

    @Test
    public void shouldUploadUserImage() throws IOException {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("Test file", new byte[0]);
        //WHEN
        ResponseEntity<String> responseEntity = userFacade.uploadUserImage(multipartFile);
        //THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(PICTURE_SAVED_TO_SERVER, responseEntity.getBody());
        //CLEANUP
        Files.deleteIfExists(Paths.get(USER_FOLDER + "/1.png"));
    }

    @Test
    public void shouldNotChangePasswordAndThrowUserNotFoundException() {
        //GIVEN
        PasswordRequest passwordRequest = createPasswordRequest("password", "testPassword", "testPassword");
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> userFacade.changePassword(passwordRequest));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldNotResetPasswordAndThrowUserNotFoundByIdException() {
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> userFacade.resetPassword("test@Gmail.com"));
        //THEN
        assertEquals("Could not find user by: test@Gmail.com", userNotFoundException.getMessage());
    }

    @Test
    public void shouldDeleteUser() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        //WHEN
        ResponseEntity<String> responseEntity = userFacade.deleteUser(user.getLogin());
        //THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals("User Deleted Successfully!", responseEntity.getBody());
        assertEquals(2, userServiceDb.getAllUsers().size());
    }

    @Test
    public void shouldNotDeleteUserAndThrowUserNotFoundException() {
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> userFacade.deleteUser("login"));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }
}
