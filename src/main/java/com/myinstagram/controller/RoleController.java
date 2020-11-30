package com.myinstagram.controller;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.role.RoleRequest;
import com.myinstagram.facade.RoleFacade;
import com.myinstagram.mapper.RoleMapper;
import com.myinstagram.service.RoleServiceDb;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/roles")
public final class RoleController {
    private final RoleMapper roleMapper;
    private final RoleServiceDb roleServiceDb;
    private final RoleFacade roleFacade;

    @GetMapping
    public List<RoleDto> getRoles() {
        return roleMapper.mapToRolesDto(roleServiceDb.getAllRoles());
    }

    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable final Long id) {
        return roleMapper.mapToRoleDto(roleServiceDb.getRoleById(id)
                                               .orElseThrow(() -> new RuntimeException("Role doesn't exist in database!")));
    }

    @PostMapping("/assign")
    public RoleDto assignRoleToUser(@RequestBody final RoleRequest roleRequest) {
        return roleFacade.assignRoleToUser(roleRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteRoleById(@PathVariable final Long id) {
        roleServiceDb.deleteRoleById(id);
    }
}
