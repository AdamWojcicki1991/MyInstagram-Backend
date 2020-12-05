package com.myinstagram.validator;

import com.myinstagram.domain.dto.RoleRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.service.RoleServiceDb;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.service.VerificationTokenServiceDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

import static com.myinstagram.domain.enums.RoleType.*;
import static com.myinstagram.domain.enums.UserStatus.BANNED;
import static com.myinstagram.util.DataFixture.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
public class UserValidatorTestSuite {
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private RoleServiceDb roleServiceDb;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private VerificationTokenServiceDb verificationTokenServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login_1", "email1@gmail.com"));
        User anotherUser = userServiceDb.saveUser(createUser("login_2", "email2@gmail.com"));
        roleServiceDb.saveRole(createRole(MODERATOR, user));
        roleServiceDb.saveRole(createRole(ADMIN, user, anotherUser));
        roleServiceDb.saveRole(createRole(USER, anotherUser));
        roleServiceDb.saveRole(createRole(MODERATOR, anotherUser));
    }

    @Test
    public void isUserValidated() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        //WHEN
        boolean userValidated = userValidator.isUserValidated(user);
        //THEN
        assertTrue(userValidated);
    }

    @Test
    public void isUserNotValidated() {
        //GIVEN
        User userDisabled = createUser("login_1", "email1@gmail.com").toBuilder().enabled(false).build();
        User userBanned = createUser("login_1", "email1@gmail.com").toBuilder().userStatus(BANNED).build();
        //WHEN
        boolean userDisabledValidated = userValidator.isUserValidated(userDisabled);
        boolean userBannedValidated = userValidator.isUserValidated(userBanned);
        //THEN
        assertFalse(userDisabledValidated);
        assertFalse(userBannedValidated);
    }

    @Test
    public void isUserValidatedToLikePost() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        Post firstPost = createPost(user, Instant.now());
        Post secondPost = createPost(user, Instant.now().plus(1, DAYS));
        Post thirdPost = createPost(user, Instant.now().plus(2, DAYS));
        User userWithPosts = user.toBuilder().likedPosts(List.of(firstPost, secondPost)).build();
        //WHEN
        boolean userValidatedToLikePost = userValidator.isUserValidatedToLikePost(userWithPosts, thirdPost);
        //THEN
        assertTrue(userValidatedToLikePost);
    }

    @Test
    public void isUserNotValidatedToLikePost() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        Post firstPost = createPost(user, Instant.now());
        Post secondPost = createPost(user, Instant.now().plus(1, DAYS));
        Post thirdPost = createPost(user, Instant.now().plus(2, DAYS));
        User userWithPosts = user.toBuilder().likedPosts(List.of(firstPost, secondPost, thirdPost)).build();
        //WHEN
        boolean userNotValidatedToLikePost = userValidator.isUserValidatedToLikePost(userWithPosts, thirdPost);
        //THEN
        assertFalse(userNotValidatedToLikePost);
    }

    @Test
    public void isUserValidatedToUnlikePost() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        Post firstPost = createPost(user, Instant.now());
        Post secondPost = createPost(user, Instant.now().plus(1, DAYS));
        Post thirdPost = createPost(user, Instant.now().plus(2, DAYS));
        User userWithPosts = user.toBuilder().likedPosts(List.of(firstPost, secondPost, thirdPost)).build();
        //WHEN
        boolean userValidatedToUnlikePost = userValidator.isUserValidatedToUnlikePost(userWithPosts, thirdPost);
        //THEN
        assertTrue(userValidatedToUnlikePost);
    }

    @Test
    public void isUserNotValidatedToUnlikePost() {
        //GIVEN
        User user = createUser("login_1", "email1@gmail.com");
        Post firstPost = createPost(user, Instant.now());
        Post secondPost = createPost(user, Instant.now().plus(1, DAYS));
        Post thirdPost = createPost(user, Instant.now().plus(2, DAYS));
        User userWithPosts = user.toBuilder().likedPosts(List.of(firstPost, secondPost)).build();
        //WHEN
        boolean userNotValidatedToUnlikePost = userValidator.isUserValidatedToUnlikePost(userWithPosts, thirdPost);
        //THEN
        assertFalse(userNotValidatedToUnlikePost);
    }

    @Test
    public void isUserValidateToAssignRole() {
        //GIVEN
        RoleRequest roleRequest = createRoleRequest(USER);
        User user = userServiceDb.getUserByLogin("login_1").get();
        //WHEN
        boolean userValidateToAssignRole = userValidator.isUserValidateToAssignRole(user, roleRequest);
        //THEN
        assertTrue(userValidateToAssignRole);
    }

    @Test
    public void isUserNotValidateToAssignRole() {
        //GIVEN
        RoleRequest roleRequest = createRoleRequest(MODERATOR);
        User user = userServiceDb.getUserByLogin("login_1").get();
        //WHEN
        boolean userNotValidateToAssignRole = userValidator.isUserValidateToAssignRole(user, roleRequest);
        //THEN
        assertFalse(userNotValidateToAssignRole);
    }

    @Test
    public void hasUserVerificationToken() {
        //GIVEN
        User user = userServiceDb.getUserByLogin("login_1").get();
        verificationTokenServiceDb.saveVerificationToken(createVerificationToken(user, "token"));
        //WHEN
        boolean hasUserVerificationToken = userValidator.hasUserVerificationToken(user);
        //THEN
        assertTrue(hasUserVerificationToken);
    }

    @Test
    public void hasNotUserVerificationToken() {
        //GIVEN
        User user = userServiceDb.getUserByLogin("login_1").get();
        //WHEN
        boolean hasNotUserVerificationToken = userValidator.hasUserVerificationToken(user);
        //THEN
        assertFalse(hasNotUserVerificationToken);
    }

    @Test
    public void hasUserRoles() {
        //GIVEN
        User user = userServiceDb.getUserByLogin("login_1").get();
        roleServiceDb.saveRole(createRole(MODERATOR, user));
        //WHEN
        boolean hasUserRoles = userValidator.hasUserRoles(user);
        //THEN
        assertTrue(hasUserRoles);
    }

    @Test
    public void hasNotUserRoles() {
        //GIVEN
        User user = userServiceDb.getUserByLogin("login_1").get();
        //WHEN
        boolean hasNotUserRoles = userValidator.hasUserRoles(user);
        //THEN
        assertFalse(hasNotUserRoles);
    }
}
