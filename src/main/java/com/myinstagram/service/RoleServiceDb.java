package com.myinstagram.service;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.util.RoleType;
import com.myinstagram.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class RoleServiceDb {
    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(final Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> getRoleByRoleType(final RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }

    public Role saveRole(final Role role) {
        return roleRepository.save(role);
    }

    public void deleteRoleById(final Long id) {
        roleRepository.deleteById(id);
    }
}
