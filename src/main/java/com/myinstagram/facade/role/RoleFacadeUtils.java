package com.myinstagram.facade.role;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.dto.RoleRequest;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.role.RoleAssignException;
import com.myinstagram.exceptions.custom.role.RoleNotFoundException;
import com.myinstagram.mapper.RoleMapper;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
class RoleFacadeUtils {
    private final RoleMapper roleMapper;
    private final RoleServiceDb roleServiceDb;
    private final UserValidator userValidator;

    ResponseEntity<RoleDto> assignRoleIfUserIsValidated(final RoleRequest roleRequest, final User userFromDb) {
        if (userValidator.isUserValidateToAssignRole(userFromDb, roleRequest)) {
            RoleDto roleDto = roleMapper.mapToRoleDto(roleServiceDb.saveRole(Role.builder()
                                                                                     .roleType(roleRequest.getRoleType())
                                                                                     .users(Set.of(userFromDb))
                                                                                     .build()));
            log.info("Role assigned successfully!");
            return new ResponseEntity<>(roleDto, OK);
        } else {
            throw new RoleAssignException(roleRequest.getRoleType());
        }
    }

    ResponseEntity<String> deleteRoleIfExists(final Long id) {
        try {
            roleServiceDb.deleteRoleById(id);
            log.info("Role deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            throw new RoleNotFoundException(id);
        }
        return new ResponseEntity<>("Role Deleted Successfully!!!", OK);
    }
}
