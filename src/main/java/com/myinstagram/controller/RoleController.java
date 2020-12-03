package com.myinstagram.controller;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.dto.RoleRequest;
import com.myinstagram.facade.role.RoleFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/roles")
public final class RoleController {
    private final RoleFacade roleFacade;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles() {
        return roleFacade.getRoles();
    }

    @GetMapping("/{login}")
    public ResponseEntity<List<RoleDto>> getRolesByLogin(@PathVariable final String login) {
        return roleFacade.getRolesByLogin(login);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable final Long id) {
        return roleFacade.getRoleById(id);
    }

    @PostMapping("/assign")
    public ResponseEntity<RoleDto> assignRoleToUser(@RequestBody final RoleRequest roleRequest) {
        return roleFacade.assignRoleToUser(roleRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoleById(@PathVariable final Long id) {
        return roleFacade.deleteRoleById(id);
    }
}
