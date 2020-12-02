package com.myinstagram.service;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.myinstagram.domain.enums.RoleType.*;
import static com.myinstagram.util.DataFixture.createRole;
import static com.myinstagram.util.DataFixture.createUser;
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
        roleServiceDb.saveRole(createRole(MODERATOR, user));
        roleServiceDb.saveRole(createRole(ADMIN, user, anotherUser));
        roleServiceDb.saveRole(createRole(USER, anotherUser));
        roleServiceDb.saveRole(createRole(MODERATOR, anotherUser));
    }

    @Test
    public void shouldGetAllRoles() {
        //WHEN
        List<Role> roles = roleServiceDb.getAllRoles();
        //THEN
        assertEquals(4, roles.size());
    }

    @Test
    public void shouldGetRolesByRoleType() {
        //WHEN
        List<Role> roles = roleServiceDb.getRolesByRoleType(USER);
        //THEN
        assertEquals(1, roles.size());
    }

    @Test
    public void shouldNotGetRolesByRoleTypeWhenRoleIsNotPresentInDataBase() {
        //WHEN
        List<Role> roles = roleServiceDb.getRolesByRoleType(NO_ROLE);
        //THEN
        assertTrue(roles.isEmpty());
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

    @Test
    public void shouldDeleteRole() {
        //GIVEN
        Role role = roleServiceDb.getAllRoles().get(0);
        //WHEN
        roleServiceDb.deleteRole(role);
        //THEN
        assertEquals(3, roleServiceDb.getAllRoles().size());
        assertEquals(Optional.empty(), roleServiceDb.getRoleById(role.getId()));
    }
}
