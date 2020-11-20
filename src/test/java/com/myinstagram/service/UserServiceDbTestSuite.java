package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.myinstagram.util.TestDataFixture.createUser;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class UserServiceDbTestSuite {
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
    public void shouldGetAllUsers() {
        //WHEN
        List<User> users = userServiceDb.getAllUsers();
        //THEN
        assertEquals(3, users.size());
    }

    @Test
    public void shouldGetAllUsersByLoginContaining() {
        //WHEN
        List<User> users = userServiceDb.getAllUsersByLoginContaining("login");
        //THEN
        assertEquals(2, users.size());
    }

    @Test
    public void shouldGetUserByUserId() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        //WHEN
        User saveUser = userServiceDb.getUserById(user.getId()).get();
        //THEN
        assertEquals(user, saveUser);
    }

    @Test
    public void shouldGetUserByEmail() {
        //WHEN
        User saveUser = userServiceDb.getUserByEmail("email1@gmail.com").get();
        //THEN
        assertEquals("email1@gmail.com", saveUser.getEmail());
    }

    @Test
    public void shouldNotGetUserByEmail() {
        //WHEN
        NoSuchElementException noSuchElementException = assertThrows(
                NoSuchElementException.class,
                () -> userServiceDb.getUserByEmail("email@gmail.com").get());
        //THEN
        assertEquals("No value present", noSuchElementException.getMessage());
    }

    @Test
    public void shouldGetUserByLogin() {
        //WHEN
        User saveUser = userServiceDb.getUserByLogin("login_1").get();
        //THEN
        assertEquals("login_1", saveUser.getLogin());
    }

    @Test
    public void shouldNotGetUserByLogin() {
        //WHEN
        NoSuchElementException noSuchElementException = assertThrows(
                NoSuchElementException.class,
                () -> userServiceDb.getUserByLogin("login").get());
        //THEN
        assertEquals("No value present", noSuchElementException.getMessage());
    }

    @Test
    public void shouldSaveUser() {
        //GIVEN
        User user = createUser("login", "email@gmail.com");
        //WHEN
        User saveUser = userServiceDb.saveUser(user);
        //THEN
        assertEquals(4, userServiceDb.getAllUsers().size());
        assertNotEquals(0, saveUser.getId());
    }

    @Test
    public void shouldDeleteUserById() {
        //GIVEN
        Long userId = userServiceDb.getAllUsers().get(0).getId();
        //WHEN
        userServiceDb.deleteUserById(userId);
        //THEN
        assertEquals(2, userServiceDb.getAllUsers().size());
        assertEquals(Optional.empty(), userServiceDb.getUserById(userId));
    }
}
