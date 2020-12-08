package com.myinstagram.util;

import com.myinstagram.domain.dto.UserRequest;
import com.myinstagram.domain.entity.User;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.SECONDS;

public final class UserSupportDataFixture {

    private UserSupportDataFixture() {
    }

    public static User updateUserPassword(final User user, final String encryptedPassword) {
        return user.toBuilder().password(encryptedPassword).build();
    }

    public static User updateUser(final User user, final UserRequest userRequest) {
        return user.toBuilder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .description(userRequest.getDescription())
                .updateDate(Instant.now().truncatedTo(SECONDS))
                .build();
    }
}
