package com.myinstagram.facade;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.role.RoleRequest;
import com.myinstagram.mapper.RoleMapper;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleFacade {
    private final RoleMapper roleMapper;
    private final RoleServiceDb roleServiceDb;
    private final UserServiceDb userServiceDb;

    public RoleDto assignRoleToUser(@RequestBody final RoleRequest roleRequest) {
        User user = userServiceDb.getUserByLogin(roleRequest.getLogin()).orElseThrow(() -> new RuntimeException());
        List<User> users = roleServiceDb.getRolesByRoleType(roleRequest.getRoleType()).stream()
                .flatMap(role -> role.getUsers().stream())
                .filter(u -> u.getId().equals(user.getId()))
                .collect(Collectors.toList());
        if (users.isEmpty()) {
            return roleMapper.mapToRoleDto(roleServiceDb.saveRole(Role.builder()
                                                                          .roleType(roleRequest.getRoleType())
                                                                          .users(Set.of(user))
                                                                          .build()));
        } else {
            throw new RuntimeException();
        }
    }
}
