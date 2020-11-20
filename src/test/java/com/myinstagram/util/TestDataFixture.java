package com.myinstagram.util;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.util.RoleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static com.myinstagram.domain.util.UserStatus.ACTIVE;

public final class TestDataFixture {

    private TestDataFixture() {
    }

    public static User createUser() {
        return User.builder()
                .userName("User")
                .login("login")
                .password("Password")
                .email("email@gmail.com")
                .description("Description")
                .createDate(LocalDate.now())
                .userStatus(ACTIVE)
                .enabled(true)
                .posts(new ArrayList<>())
                .roles(new HashSet<>())
                .build();
    }

    public static Post createPost(User user) {
        return Post.builder()
                .postName("Post")
                .caption("Sign")
                .url("URL")
                .imageSerialNumber(0L)
                .likesCount(0L)
                .postDate(LocalDate.now())
                .user(user)
                .comments(new ArrayList<>())
                .build();
    }

    public static Comment createComment(Post post) {
        return Comment.builder()
                .commentName("Comment")
                .content("Content")
                .commentDate(LocalDate.now())
                .post(post)
                .build();
    }

    public static Role createRole(RoleType roleType) {
        return Role.builder()
                .roleType(roleType)
                .users(new HashSet<>())
                .build();
    }
}
