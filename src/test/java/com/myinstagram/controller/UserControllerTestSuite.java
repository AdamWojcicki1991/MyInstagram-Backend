package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.domain.dto.PasswordRequest;
import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.facade.user.UserFacade;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.security.service.AuthenticationService;
import com.myinstagram.security.service.CustomUserDetailsService;
import com.myinstagram.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;
import static com.myinstagram.domain.util.Constants.PICTURE_SAVED_TO_SERVER;
import static com.myinstagram.util.DtoDataFixture.createUserDto;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createPasswordRequest;
import static com.myinstagram.util.RequestDataFixture.createUserRequest;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "user", password = "password")
@WebMvcTest(UserController.class)
public class UserControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private UserFacade userFacade;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/users | GET")
    public void shouldGetUsers() throws Exception {
        //GIVEN
        UserDto firstUserDto = createUserDto("login1", "test1@gmail.com").toBuilder().id(1L).city("Poznan").build();
        UserDto secondUserDto = createUserDto("login2", "test2@gmail.com").toBuilder().id(2L).city("Poznan").build();
        UserDto thirdUserDto = createUserDto("login3", "test3@gmail.com").toBuilder().id(3L).city("Poznan").build();
        ResponseEntity<List<UserDto>> responseEntities = new ResponseEntity<>(
                List.of(firstUserDto, secondUserDto, thirdUserDto), OK);
        given(userFacade.getUsers()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/users"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userName", is("User")))
                .andExpect(jsonPath("$[0].login", is("login1")))
                .andExpect(jsonPath("$[0].password", is("Password")))
                .andExpect(jsonPath("$[0].email", is("test1@gmail.com")))
                .andExpect(jsonPath("$[0].city", is("Poznan")))
                .andExpect(jsonPath("$[0].description", is("Description")))
                .andExpect(jsonPath("$[0].createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$[0].enabled", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].userName", is("User")))
                .andExpect(jsonPath("$[1].login", is("login2")))
                .andExpect(jsonPath("$[1].password", is("Password")))
                .andExpect(jsonPath("$[1].email", is("test2@gmail.com")))
                .andExpect(jsonPath("$[1].city", is("Poznan")))
                .andExpect(jsonPath("$[1].description", is("Description")))
                .andExpect(jsonPath("$[1].createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[1].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[1].userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$[1].enabled", is(true)))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].userName", is("User")))
                .andExpect(jsonPath("$[2].login", is("login3")))
                .andExpect(jsonPath("$[2].password", is("Password")))
                .andExpect(jsonPath("$[2].email", is("test3@gmail.com")))
                .andExpect(jsonPath("$[2].city", is("Poznan")))
                .andExpect(jsonPath("$[2].description", is("Description")))
                .andExpect(jsonPath("$[2].createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[2].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[2].userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$[2].enabled", is(true)));
    }

    @Test
    @DisplayName("/users | GET")
    public void shouldGetEmptyUsers() throws Exception {
        //GIVEN
        ResponseEntity<List<UserDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        given(userFacade.getUsers()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/users"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/users/{login} | GET")
    public void shouldGetUsersByLogin() throws Exception {
        //GIVEN
        UserDto firstUserDto = createUserDto("login1", "test1@gmail.com").toBuilder().id(1L).city("Poznan").build();
        UserDto secondUserDto = createUserDto("login2", "test2@gmail.com").toBuilder().id(2L).city("Poznan").build();
        UserDto thirdUserDto = createUserDto("login3", "test3@gmail.com").toBuilder().id(3L).city("Poznan").build();
        ResponseEntity<List<UserDto>> responseEntities = new ResponseEntity<>(List.of(firstUserDto), OK);
        given(userFacade.getUsersByLogin("login1")).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/users/login1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].userName", is("User")))
                .andExpect(jsonPath("$[0].login", is("login1")))
                .andExpect(jsonPath("$[0].password", is("Password")))
                .andExpect(jsonPath("$[0].email", is("test1@gmail.com")))
                .andExpect(jsonPath("$[0].city", is("Poznan")))
                .andExpect(jsonPath("$[0].description", is("Description")))
                .andExpect(jsonPath("$[0].createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$[0].userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$[0].enabled", is(true)));
    }

    @Test
    @DisplayName("/users/{login} | GET")
    public void shouldGetEmptyUsersByLogin() throws Exception {
        //GIVEN
        ResponseEntity<List<UserDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        given(userFacade.getUsersByLogin("login")).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/users/login"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/users/id/{id} | GET")
    public void shouldGetUserById() throws Exception {
        //GIVEN
        UserDto userDto = createUserDto("login1", "test1@gmail.com").toBuilder().id(1L).city("Poznan").build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        given(userFacade.getUserById(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/users/id/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("User")))
                .andExpect(jsonPath("$.login", is("login1")))
                .andExpect(jsonPath("$.password", is("Password")))
                .andExpect(jsonPath("$.email", is("test1@gmail.com")))
                .andExpect(jsonPath("$.city", is("Poznan")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @DisplayName("/users/login/{login} | GET")
    public void shouldGetUserByLogin() throws Exception {
        //GIVEN
        UserDto userDto = createUserDto("login1", "test1@gmail.com").toBuilder().id(1L).city("Poznan").build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        given(userFacade.getUserByLogin("login1")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/users/login/login1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("User")))
                .andExpect(jsonPath("$.login", is("login1")))
                .andExpect(jsonPath("$.password", is("Password")))
                .andExpect(jsonPath("$.email", is("test1@gmail.com")))
                .andExpect(jsonPath("$.city", is("Poznan")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @DisplayName("/users/mail/{mail} | GET")
    public void shouldGetUserByEmail() throws Exception {
        //GIVEN
        UserDto userDto = createUserDto("login1", "test1@gmail.com").toBuilder().id(1L).city("Poznan").build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        given(userFacade.getUserByMail("test1@gmail.com")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/users/mail/test1@gmail.com"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("User")))
                .andExpect(jsonPath("$.login", is("login1")))
                .andExpect(jsonPath("$.password", is("Password")))
                .andExpect(jsonPath("$.email", is("test1@gmail.com")))
                .andExpect(jsonPath("$.city", is("Poznan")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @DisplayName("/users/upload | POST")
    public void shouldUploadUserImage() throws Exception {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("image",
                                                                "hello.txt",
                                                                APPLICATION_JSON_VALUE,
                                                                "MyInstagram!".getBytes());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(PICTURE_SAVED_TO_SERVER, OK);
        given(userFacade.uploadUserImage(multipartFile)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(multipart("/users/upload")
                                .file(multipartFile)
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8"))
                .andExpect(status().is(200))
                .andExpect(content().string(PICTURE_SAVED_TO_SERVER));
    }

    @Test
    @DisplayName("/users/update | PUT")
    public void shouldUpdateProfile() throws Exception {
        //GIVEN
        UserRequest userRequest = createUserRequest(createUser("login", "test@gmail.com"));
        UserDto userDto = createUserDto("login1", "test1@gmail.com").toBuilder()
                .id(1L)
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .description(userRequest.getDescription())
                .city("Poznan")
                .build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        String jsonContent = gson.toJson(userRequest);
        given(userFacade.updateProfile(userRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(put("/users/update")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("Test User")))
                .andExpect(jsonPath("$.login", is("login1")))
                .andExpect(jsonPath("$.password", is("Password")))
                .andExpect(jsonPath("$.email", is("test@gmail.com")))
                .andExpect(jsonPath("$.city", is("Poznan")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @DisplayName("/users/changePassword | PUT")
    public void shouldChangePassword() throws Exception {
        //GIVEN
        PasswordRequest passwordRequest = createPasswordRequest("password", "newPassword", "newPassword");
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Password Changed Successfully!", OK);
        String jsonContent = gson.toJson(passwordRequest);
        given(userFacade.changePassword(passwordRequest)).willReturn(responseEntity);
        mockMvc.perform(put("/users/changePassword")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(content().string("Password Changed Successfully!"));
    }

    @Test
    @DisplayName("/users/resetPassword/{mail} | PUT")
    public void shouldResetPassword() throws Exception {
        //GIVEN
        UserDto userDto = createUserDto("login1", "test1@gmail.com").toBuilder()
                .id(1L)
                .password("Reset password")
                .city("Poznan")
                .build();
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(userDto, OK);
        given(userFacade.resetPassword("test1@gmail.com")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(put("/users/resetPassword/test1@gmail.com"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userName", is("User")))
                .andExpect(jsonPath("$.login", is("login1")))
                .andExpect(jsonPath("$.password", is("Reset password")))
                .andExpect(jsonPath("$.email", is("test1@gmail.com")))
                .andExpect(jsonPath("$.city", is("Poznan")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.createDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.updateDate", is(now().truncatedTo(SECONDS).toString())))
                .andExpect(jsonPath("$.userStatus", is(ACTIVE.toString())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @DisplayName("/users/{login} | DELETE")
    public void shouldDeleteUser() throws Exception {
        //GIVEN
        ResponseEntity<String> responseEntity = new ResponseEntity<>("User Deleted Successfully!", OK);
        given(userFacade.deleteUser("login")).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(delete("/users/login"))
                .andExpect(status().is(200))
                .andExpect(content().string("User Deleted Successfully!"));
    }
}
