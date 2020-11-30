package com.myinstagram.domain.dto;

import com.myinstagram.domain.enums.RoleType;
import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public final class RoleDto {
    private final Long id;
    private final RoleType roleType;
}
