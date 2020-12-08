package com.myinstagram.domain.dto;

import com.myinstagram.domain.enums.UserStatus;
import lombok.*;

import java.time.Instant;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public final class UserDto {
    private final Long id;
    private final String userName;
    private final String login;
    private final String password;
    private final String email;
    private final String city;
    private final String description;
    private final Instant createDate;
    private final Instant updateDate;
    private final UserStatus userStatus;
    private final boolean enabled;
}
