package com.myinstagram.util;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.entity.User;

import java.time.LocalDate;

import static com.myinstagram.domain.util.UserStatus.ACTIVE;

public final class TestDataFixture {

    private TestDataFixture() {
    }

    public static User createUser() {
        return User.builder()
                .createDate(LocalDate.now())
                .description("Description")
                .email("email@gmail.com")
                .enabled(true)
                .login("login")
                .password("Password")
                .userName("User")
                .userStatus(ACTIVE)
                .build();
    }

    public static Post createPost(User user) {
        return Post.builder()
                .caption("Sign")
                .imageSerialNumber(0L)
                .likesCount(0L)
                .postDate(LocalDate.now())
                .postName("Post")
                .url("URL")
                .user(user)
                .build();
    }

    public static Comment createComment(Post post) {
        return Comment.builder()
                .commentDate(LocalDate.now())
                .commentName("Comment")
                .content("Content")
                .post(post)
                .build();
    }

    public static Role createRole() {
        return Role.builder()
                .roleName("Role")
                .build();
    }
}
