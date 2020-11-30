package com.myinstagram.domain.auth;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class LoginRequest {
    private String login;
    private String password;
}
