package com.myinstagram.facade;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.role.RoleRequest;
import com.myinstagram.exceptions.RoleAssignException;
import com.myinstagram.exceptions.RoleNotFoundException;
import com.myinstagram.exceptions.UserNotFoundException;
import com.myinstagram.mapper.RoleMapper;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RoleFacade {
    private final RoleMapper roleMapper;
    private final RoleServiceDb roleServiceDb;
    private final UserServiceDb userServiceDb;

    public ResponseEntity<List<RoleDto>> getRoles() {
        log.info("Get available roles!");
        List<RoleDto> roles = roleMapper.mapToRolesDto(roleServiceDb.getAllRoles());
        return new ResponseEntity<>(roles, OK);
    }

    public ResponseEntity<RoleDto> getRole(final Long id) {
        log.info("Get available role by id: " + id);
        RoleDto roleDto = roleMapper.mapToRoleDto(roleServiceDb.getRoleById(id)
                                                          .orElseThrow(() -> new RoleNotFoundException(id)));
        return new ResponseEntity<>(roleDto, OK);
    }

    public ResponseEntity<RoleDto> assignRoleToUser(final RoleRequest roleRequest) {
        log.info("Try assign user: " + roleRequest.getLogin() + " to role: " + roleRequest.getRoleType());
        User userFromDb = userServiceDb.getUserByLogin(roleRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(roleRequest.getLogin()));

        List<User> users = roleServiceDb.getRolesByRoleType(roleRequest.getRoleType()).stream()
                .flatMap(role -> role.getUsers().stream())
                .filter(user -> userFromDb.getId().equals(user.getId()))
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            RoleDto roleDto = roleMapper.mapToRoleDto(roleServiceDb.saveRole(Role.builder()
                                                                                     .roleType(roleRequest.getRoleType())
                                                                                     .users(Set.of(userFromDb))
                                                                                     .build()));
            return new ResponseEntity<>(roleDto, OK);
        } else {
            throw new RoleAssignException(roleRequest.getRoleType());
        }
    }

    public ResponseEntity<String> deleteRoleById(final Long id) {
        log.info("Delete available role by id: " + id);
        try {
            roleServiceDb.deleteRoleById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RoleNotFoundException(id);
        }
        return new ResponseEntity<>("Role Deleted Successfully!!!", OK);
    }
}
