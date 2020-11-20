package com.myinstagram.service;

import com.myinstagram.domain.entity.User;
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

    @Test
    public void shouldGetAllUsers() {
        //GIVEN
        userServiceDb.saveUser(createUser());
        //WHEN
        List<User> users = userServiceDb.getAllUsers();
        //THEN
        assertEquals(1, users.size());
    }

    @Test
    public void shouldGetAllUsersByLoginContaining() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser());
        //WHEN
        List<User> users = userServiceDb.getAllUsersByLoginContaining("login");
        //THEN
        assertEquals(1, users.size());
        assertEquals(user.getLogin(), users.get(0).getLogin());
    }

    @Test
    public void shouldGetUserByUserId() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser());
        //WHEN
        User saveUser = userServiceDb.getUserById(user.getId()).get();
        //THEN
        assertEquals(user.getId(), saveUser.getId());
        assertEquals(user.getLogin(), saveUser.getLogin());
    }

    @Test
    public void shouldGetUserByEmail() {
        //GIVEN
        userServiceDb.saveUser(createUser());
        //WHEN
        User saveUser = userServiceDb.getUserByEmail("email@gmail.com").get();
        //THEN
        assertEquals("email@gmail.com", saveUser.getEmail());
    }

    @Test
    public void shouldNotGetUserByEmail() {
        //GIVEN
        userServiceDb.saveUser(createUser());
        //WHEN
        NoSuchElementException noSuchElementException = assertThrows(
                NoSuchElementException.class,
                () -> userServiceDb.getUserByEmail("default@gmail.com").get());
        //THEN
        assertEquals("No value present", noSuchElementException.getMessage());
    }

    @Test
    public void shouldGetUserByLogin() {
        //GIVEN
        userServiceDb.saveUser(createUser());
        //WHEN
        User saveUser = userServiceDb.getUserByLogin("login").get();
        //THEN
        assertEquals("login", saveUser.getLogin());
    }

    @Test
    public void shouldNotGetUserByLogin() {
        //GIVEN
        userServiceDb.saveUser(createUser());
        //WHEN
        NoSuchElementException noSuchElementException = assertThrows(
                NoSuchElementException.class,
                () -> userServiceDb.getUserByLogin("default").get());
        //THEN
        assertEquals("No value present", noSuchElementException.getMessage());
    }

    @Test
    public void shouldSaveUser() {
        //GIVEN
        User user = createUser();
        //WHEN
        User saveUser = userServiceDb.saveUser(user);
        //THEN
        assertNotEquals(0, saveUser.getId());
    }

    @Test
    public void shouldDeleteUserById() {
        //GIVEN
        Long userId = userServiceDb.saveUser(createUser()).getId();
        //WHEN
        userServiceDb.deleteUserById(userId);
        //THEN
        assertEquals(Optional.empty(), userServiceDb.getUserById(userId));
    }
}
