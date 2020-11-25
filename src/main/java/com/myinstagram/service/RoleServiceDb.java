package com.myinstagram.service;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.enums.RoleType;
import com.myinstagram.repository.RoleDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceDb {
    private final RoleDbRepository roleDbRepository;

    public List<Role> getAllRoles() {
        return roleDbRepository.findAll();
    }

    public Optional<Role> getRoleById(final Long id) {
        return roleDbRepository.findById(id);
    }

    public Optional<Role> getRoleByRoleType(final RoleType roleType) {
        return roleDbRepository.findByRoleType(roleType);
    }

    public Role saveRole(final Role role) {
        return roleDbRepository.save(role);
    }

    public void deleteRoleById(final Long id) {
        roleDbRepository.deleteById(id);
    }
}
