package com.myinstagram.util;

import com.myinstagram.domain.entity.*;
import com.myinstagram.domain.enums.RoleType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;
import static java.time.temporal.ChronoUnit.SECONDS;

public final class EntityDataFixture {

    private EntityDataFixture() {
    }

    public static User createUser(final String login, final String mail) {
        return User.builder()
                .userName("User")
                .login(login)
                .password("Password")
                .email(mail)
                .city("Poznan")
                .description("Description")
                .createDate(Instant.now().truncatedTo(SECONDS))
                .updateDate(Instant.now().truncatedTo(SECONDS))
                .userStatus(ACTIVE)
                .enabled(true)
                .posts(new ArrayList<>())
                .roles(new HashSet<>())
                .likedPosts(new ArrayList<>())
                .build();
    }

    public static Post createPost(final User user, final Instant postDate) {
        return Post.builder()
                .postName("Post")
                .caption("Sign")
                .url("URL")
                .imageSerialNumber(0L)
                .likesCount(0L)
                .postDate(postDate)
                .updateDate(Instant.now().truncatedTo(SECONDS))
                .user(user)
                .comments(new ArrayList<>())
                .build();
    }

    public static Comment createComment(final Post post) {
        return Comment.builder()
                .commentName("Comment")
                .content("Content")
                .commentDate(Instant.now().truncatedTo(SECONDS))
                .updateDate(Instant.now().truncatedTo(SECONDS))
                .post(post)
                .build();
    }

    public static Role createRole(final RoleType roleType, final User... users) {
        return Role.builder()
                .roleType(roleType)
                .users(new HashSet<>(Set.of(users)))
                .build();
    }

    public static RefreshToken createRefreshToken(final String token) {
        return RefreshToken.builder()
                .token(token)
                .createDate(Instant.now().truncatedTo(SECONDS))
                .build();
    }

    public static VerificationToken createVerificationToken(final User user, final String token) {
        return VerificationToken.builder()
                .token(token)
                .user(user)
                .expirationDate(Instant.now().truncatedTo(SECONDS))
                .build();
    }
}
