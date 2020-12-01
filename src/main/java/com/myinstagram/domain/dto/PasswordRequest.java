package com.myinstagram.domain.dto;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class PasswordRequest {
    private String login;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
