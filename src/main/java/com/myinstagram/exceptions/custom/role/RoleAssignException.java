package com.myinstagram.exceptions.custom.role;

import com.myinstagram.domain.enums.RoleType;

public class RoleAssignException extends RuntimeException {
    public RoleAssignException(RoleType roleType) {
        super("User is already assigned to role: " + roleType);
    }
}
