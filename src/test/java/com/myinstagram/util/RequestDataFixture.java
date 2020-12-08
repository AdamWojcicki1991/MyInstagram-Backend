package com.myinstagram.util;

import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.dto.*;
import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.enums.RoleType;

public final class RequestDataFixture {

    private RequestDataFixture() {
    }

    public static CommentRequest createCommentRequest() {
        return CommentRequest.builder()
                .login("Test Comment")
                .content("Test Content")
                .postId(1L)
                .build();
    }

    public static UpdateCommentRequest createUpdateCommentRequest(final Comment comment) {
        return UpdateCommentRequest.builder()
                .commentId(comment.getId())
                .commentName("Test Comment")
                .content("Test Content")
                .build();
    }

    public static PostRequest createPostRequest(final String postName, final String caption) {
        return PostRequest.builder()
                .login("Test login")
                .postName(postName)
                .caption(caption)
                .url("Test url")
                .build();
    }

    public static SimplePostRequest createSimplePostRequest() {
        return SimplePostRequest.builder()
                .postId(1L)
                .login("login")
                .build();
    }

    public static UpdatePostRequest createUpdatePostRequest() {
        return UpdatePostRequest.builder()
                .postId(1L)
                .postName("Test Post")
                .caption("Test caption")
                .build();
    }

    public static UserRequest createUserRequest(final User user) {
        return UserRequest.builder()
                .userId(user.getId())
                .userName("Test User")
                .email("test@gmail.com")
                .description("Test Description")
                .build();
    }

    public static PasswordRequest createPasswordRequest(final String currentPassword, final String confirmPassword, final String newPassword) {
        return PasswordRequest.builder()
                .login("login")
                .currentPassword(currentPassword)
                .confirmPassword(confirmPassword)
                .newPassword(newPassword)
                .build();
    }

    public static RegisterRequest createRegisterRequest(final String name, final String login,
                                                        final String city, final String email, final String password) {
        return RegisterRequest.builder()
                .name(name)
                .login(login)
                .city(city)
                .email(email)
                .password(password)
                .build();
    }

    public static RoleRequest createRoleRequest(final RoleType roleType) {
        return RoleRequest.builder()
                .login("login")
                .roleType(roleType)
                .build();
    }

    public static LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .login("login")
                .password("password")
                .build();
    }

    public static RefreshTokenRequest createRefreshTokenRequest() {
        return RefreshTokenRequest.builder()
                .login("login")
                .refreshToken("refreshToken")
                .build();
    }
}
