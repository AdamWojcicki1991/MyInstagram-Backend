package com.myinstagram.service;

import com.myinstagram.domain.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.myinstagram.domain.util.RoleType.ADMIN;
import static com.myinstagram.domain.util.RoleType.USER;
import static com.myinstagram.util.TestDataFixture.createRole;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class RoleServiceDbTestSuite {
    @Autowired
    private RoleServiceDb roleServiceDb;

    @Test
    public void shouldGetAllRoles() {
        //GIVEN
        roleServiceDb.saveRole(createRole(USER));
        //WHEN
        List<Role> roles = roleServiceDb.getAllRoles();
        //THEN
        assertEquals(1, roles.size());
    }

    @Test
    public void shouldGetRoleById() {
        //GIVEN
        Role role = roleServiceDb.saveRole(createRole(USER));
        //WHEN
        Role saveRole = roleServiceDb.getRoleById(role.getId()).get();
        //THEN
        assertEquals(role.getId(), saveRole.getId());
    }

    @Test
    public void shouldGetRoleByRoleType() {
        //GIVEN
        roleServiceDb.saveRole(createRole(USER));
        //WHEN
        Role saveRole = roleServiceDb.getRoleByRoleType(USER).get();
        //THEN
        assertEquals(USER, saveRole.getRoleType());
    }

    @Test
    public void shouldNotGetRoleByRoleType() {
        //GIVEN
        roleServiceDb.saveRole(createRole(USER));
        //WHEN
        NoSuchElementException noSuchElementException = assertThrows(
                NoSuchElementException.class,
                () -> roleServiceDb.getRoleByRoleType(ADMIN).get());
        //THEN
        assertEquals("No value present", noSuchElementException.getMessage());
    }

    @Test
    public void shouldSaveRole() {
        //GIVEN
        Role role = createRole(USER);
        //WHEN
        Role saveRole = roleServiceDb.saveRole(role);
        //THEN
        assertNotEquals(0, saveRole.getId());
    }

    @Test
    public void shouldDeleteRoleById() {
        //GIVEN
        Long roleId = roleServiceDb.saveRole(createRole(USER)).getId();
        //WHEN
        roleServiceDb.deleteRoleById(roleId);
        //THEN
        assertEquals(Optional.empty(), roleServiceDb.getRoleById(roleId));
    }
}
