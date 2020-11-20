package com.myinstagram.service;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.myinstagram.domain.util.RoleType.*;
import static com.myinstagram.util.TestDataFixture.createRole;
import static com.myinstagram.util.TestDataFixture.createUser;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class RoleServiceDbTestSuite {
    @Autowired
    private RoleServiceDb roleServiceDb;

    @Autowired
    private UserServiceDb userServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login_1", "email1@gmail.com"));
        User anotherUser = userServiceDb.saveUser(createUser("login_2", "email2@gmail.com"));
        roleServiceDb.saveRole(createRole(GUEST, user));
        roleServiceDb.saveRole(createRole(ADMIN, user, anotherUser));
        roleServiceDb.saveRole(createRole(USER, anotherUser));
        roleServiceDb.saveRole(createRole(GUEST, anotherUser));
    }

    @Test
    public void shouldGetAllRoles() {
        //WHEN
        List<Role> roles = roleServiceDb.getAllRoles();
        //THEN
        assertEquals(4, roles.size());
    }

    @Test
    public void shouldGetRoleById() {
        //GIVEN
        Role role = roleServiceDb.getAllRoles().get(0);
        //WHEN
        Role saveRole = roleServiceDb.getRoleById(role.getId()).get();
        //THEN
        assertEquals(role, saveRole);
    }

    @Test
    public void shouldGetRoleByRoleType() {
        //WHEN
        Role saveRole = roleServiceDb.getRoleByRoleType(USER).get();
        //THEN
        assertEquals(USER, saveRole.getRoleType());
    }

    @Test
    public void shouldNotGetRoleByRoleTypeWhenRoleIsNotPresentInDataBase() {
        //WHEN
        NoSuchElementException noSuchElementException = assertThrows(
                NoSuchElementException.class,
                () -> roleServiceDb.getRoleByRoleType(NO_ROLE).get());
        //THEN
        assertEquals("No value present", noSuchElementException.getMessage());
    }

    @Test
    public void shouldNotGetRoleByRoleTypeWhenDuplicateRoleIsSavedInDataBase() {
        //GIVEN
        roleServiceDb.saveRole(createRole(USER));
        //WHEN
        IncorrectResultSizeDataAccessException incorrectResultSizeDataAccessException = assertThrows(
                IncorrectResultSizeDataAccessException.class,
                () -> roleServiceDb.getRoleByRoleType(USER).get());
        //THEN
        assertEquals("query did not return a unique result: 2; nested exception is " +
                             "javax.persistence.NonUniqueResultException: query did not return a unique result: 2",
                     incorrectResultSizeDataAccessException.getMessage());
    }

    @Test
    public void shouldSaveRole() {
        //GIVEN
        Role role = createRole(USER);
        //WHEN
        Role saveRole = roleServiceDb.saveRole(role);
        //THEN
        assertEquals(5, roleServiceDb.getAllRoles().size());
        assertNotEquals(0, saveRole.getId());
    }

    @Test
    public void shouldDeleteRoleById() {
        //GIVEN
        Long roleId = roleServiceDb.getAllRoles().get(0).getId();
        //WHEN
        roleServiceDb.deleteRoleById(roleId);
        //THEN
        assertEquals(3, roleServiceDb.getAllRoles().size());
        assertEquals(Optional.empty(), roleServiceDb.getRoleById(roleId));
    }
}
