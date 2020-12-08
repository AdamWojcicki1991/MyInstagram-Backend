package com.myinstagram.facade.role;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.dto.RoleRequest;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.role.RoleNotFoundException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.IntStream;

import static com.myinstagram.domain.enums.RoleType.*;
import static com.myinstagram.util.EntityDataFixture.createRole;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createRoleRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.OK;

@Transactional
@SpringBootTest
public class RoleFacadeTestSuite {
    @Autowired
    private RoleFacade roleFacade;
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
    public void shouldGetRoles() {
        //GIVEN
        List<Role> roles = roleServiceDb.getAllRoles();
        //WHEN
        ResponseEntity<List<RoleDto>> rolesDtoResponseEntity = roleFacade.getRoles();
        //THEN
        assertEquals(4, rolesDtoResponseEntity.getBody().size());
        assertEquals(OK, rolesDtoResponseEntity.getStatusCode());
        IntStream.range(0, 4).forEach(integer -> {
            assertEquals(RoleDto.class, rolesDtoResponseEntity.getBody().get(integer).getClass());
            assertEquals(roles.get(integer).getId(), rolesDtoResponseEntity.getBody().get(integer).getId());
            assertEquals(roles.get(integer).getRoleType(), rolesDtoResponseEntity.getBody().get(integer).getRoleType());
        });
    }

    @Test
    public void shouldGetRolesByLogin() {
        //GIVEN
        List<Role> roles = roleServiceDb.getAllRoles();
        //WHEN
        ResponseEntity<List<RoleDto>> responseEntityRolesDtoByLogin = roleFacade.getRolesByLogin("login_1");
        //THEN
        assertEquals(2, responseEntityRolesDtoByLogin.getBody().size());
        assertEquals(OK, responseEntityRolesDtoByLogin.getStatusCode());
        IntStream.range(0, 2).forEach(integer -> {
            assertEquals(RoleDto.class, responseEntityRolesDtoByLogin.getBody().get(integer).getClass());
            assertEquals(roles.get(integer).getId(), responseEntityRolesDtoByLogin.getBody().get(integer).getId());
            assertEquals(roles.get(integer).getRoleType(), responseEntityRolesDtoByLogin.getBody().get(integer).getRoleType());
        });
    }

    @Test
    public void shouldNotGetRolesByLoginAndThrowUserNotFoundException() {
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> roleFacade.getRolesByLogin("login"));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldGetRoleById() {
        //GIVEN
        Role role = roleServiceDb.getAllRoles().get(0);
        //WHEN
        ResponseEntity<RoleDto> roleDtoResponseEntity = roleFacade.getRoleById(role.getId());
        //THEN
        assertEquals(OK, roleDtoResponseEntity.getStatusCode());
        assertEquals(RoleDto.class, roleDtoResponseEntity.getBody().getClass());
        assertEquals(role.getId(), roleDtoResponseEntity.getBody().getId());
        assertEquals(role.getRoleType(), roleDtoResponseEntity.getBody().getRoleType());
    }

    @Test
    public void shouldNotGetRoleByIdAndRoleNotFoundException() {
        //WHEN
        RoleNotFoundException roleNotFoundException = assertThrows(RoleNotFoundException.class,
                                                                   () -> roleFacade.getRoleById(123L));
        //THEN
        assertEquals("Could not find role by id: 123", roleNotFoundException.getMessage());
    }


    @Test
    public void shouldAssignRoleToUser() {
        //GIVEN
        RoleRequest roleRequest = createRoleRequest(USER).toBuilder().login("login_1").build();
        //WHEN
        ResponseEntity<RoleDto> roleDtoResponseEntity = roleFacade.assignRoleToUser(roleRequest);
        //THEN
        assertEquals(OK, roleDtoResponseEntity.getStatusCode());
        assertEquals(RoleDto.class, roleDtoResponseEntity.getBody().getClass());
        assertEquals(roleRequest.getRoleType(), roleDtoResponseEntity.getBody().getRoleType());
    }

    @Test
    public void shouldNotAssignRoleToUserAndUserNotFoundException() {
        //GIVEN
        RoleRequest roleRequest = createRoleRequest(USER);
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> roleFacade.assignRoleToUser(roleRequest));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldDeleteRoleById() {
        //GIVEN
        Role role = roleServiceDb.getAllRoles().get(0);
        //WHEN
        ResponseEntity<String> responseEntity = roleFacade.deleteRoleById(role.getId());
        //THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(3, roleServiceDb.getAllRoles().size());
    }
}
