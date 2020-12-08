package com.myinstagram.facade.post;

import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.SimplePostRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.post.PostNotFoundByLoginException;
import com.myinstagram.exceptions.custom.post.PostNotFoundException;
import com.myinstagram.exceptions.custom.user.UserValidationException;
import com.myinstagram.service.PostServiceDb;
import com.myinstagram.service.UserServiceDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

import static com.myinstagram.util.EntityDataFixture.createPost;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.createPostRequest;
import static com.myinstagram.util.RequestDataFixture.createSimplePostRequest;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class PostFacadeUtilsTestSuite {
    @Autowired
    private PostFacadeUtils postFacadeUtils;
    @Autowired
    private PostServiceDb postServiceDb;
    @Autowired
    private UserServiceDb userServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        postServiceDb.savePost(createPost(
                userServiceDb.saveUser(createUser("login1", "test1@gmail.com")),
                Instant.now()));

        postServiceDb.savePost(createPost(
                userServiceDb.saveUser(createUser("login2", "test2@gmail.com")),
                Instant.now().plus(1, DAYS)));

        postServiceDb.savePost(createPost(
                userServiceDb.saveUser(createUser("login3", "test3@gmail.com")),
                Instant.now().plus(2, DAYS)));
    }

    @Test
    public void shouldNotGetPostsIfExistsAndThrowPostNotFoundByLoginException() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login4", "test1@gmail.com"));
        //WHEN
        PostNotFoundByLoginException postNotFoundByLoginException = assertThrows(
                PostNotFoundByLoginException.class,
                () -> postFacadeUtils.getPostsIfExists(user.getLogin()));
        //THEN
        assertEquals("Could not find posts by login: login4", postNotFoundByLoginException.getMessage());
    }

    @Test
    public void shouldNotCreatePostIfUserIsAuthorizedAndThrowUserValidationException() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login4", "test1@gmail.com")).toBuilder().enabled(false).build();
        PostRequest postRequest = createPostRequest("Test name", "Test caption");
        //WHEN
        UserValidationException userValidationException = assertThrows(
                UserValidationException.class,
                () -> postFacadeUtils.createPostIfUserIsAuthorized(postRequest, user));
        //THEN
        assertEquals("User Test login is not authorized or active!", userValidationException.getMessage());
    }

    @Test
    public void shouldNotLikePostIfUserIsValidatedAndThrowUserValidationException() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        User user = userServiceDb.saveUser(createUser("login4", "test1@gmail.com"))
                .toBuilder().likedPosts(List.of(post)).build();
        SimplePostRequest simplePostRequest = createSimplePostRequest();
        //WHEN
        UserValidationException userValidationException = assertThrows(
                UserValidationException.class,
                () -> postFacadeUtils.likePostIfUserIsValidated(simplePostRequest, post, user));
        //THEN
        assertEquals("User login is not authorized or post is already liked by this user!",
                     userValidationException.getMessage());
    }

    @Test
    public void shouldNotUnlikePostIfUserIsValidatedAndThrowUserValidationException() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        User user = userServiceDb.saveUser(createUser("login4", "test1@gmail.com"));
        SimplePostRequest simplePostRequest = createSimplePostRequest();
        //WHEN
        UserValidationException userValidationException = assertThrows(
                UserValidationException.class,
                () -> postFacadeUtils.unlikePostIfUserIsValidated(simplePostRequest, post, user));
        //THEN
        assertEquals("User login is not authorized or post is already unliked by this user!",
                     userValidationException.getMessage());
    }

    @Test
    public void shouldNotDeletePostIfExistsAndThrowPostNotFoundException() {
        //WHEN
        PostNotFoundException postNotFoundException = assertThrows(
                PostNotFoundException.class,
                () -> postFacadeUtils.deletePostIfExists(123L));
        //THEN
        assertEquals("Could not find post by id: 123", postNotFoundException.getMessage());
    }
}
