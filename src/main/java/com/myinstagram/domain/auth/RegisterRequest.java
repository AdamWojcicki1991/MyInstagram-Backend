package com.myinstagram.domain.auth;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class RegisterRequest {
    private String login;
    private String password;
    private String email;
    private String city;
}
