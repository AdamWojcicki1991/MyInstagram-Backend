package com.myinstagram.domain.auth;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
    private String login;
}
