package com.myinstagram.util;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.enums.RoleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;

public final class DataFixture {

    private DataFixture() {
    }

    public static User createUser(final String login, final String mail) {
        return User.builder()
                .userName("User")
                .login(login)
                .password("Password")
                .email(mail)
                .description("Description")
                .createDate(LocalDate.now())
                .userStatus(ACTIVE)
                .enabled(true)
                .posts(new ArrayList<>())
                .roles(new HashSet<>())
                .build();
    }

    public static Post createPost(final User user, final LocalDate postDate) {
        return Post.builder()
                .postName("Post")
                .caption("Sign")
                .url("URL")
                .imageSerialNumber(0L)
                .likesCount(0L)
                .postDate(postDate)
                .user(user)
                .comments(new ArrayList<>())
                .build();
    }

    public static Comment createComment(final Post post) {
        return Comment.builder()
                .commentName("Comment")
                .content("Content")
                .commentDate(LocalDate.now())
                .post(post)
                .build();
    }

    public static Role createRole(final RoleType roleType, final User... users) {
        return Role.builder()
                .roleType(roleType)
                .users(new HashSet<>(Set.of(users)))
                .build();
    }
}
