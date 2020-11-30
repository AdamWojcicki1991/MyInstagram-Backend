package com.myinstagram.domain.role;

import com.myinstagram.domain.enums.RoleType;
import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class RoleRequest {
    private String login;
    private RoleType roleType;
}
