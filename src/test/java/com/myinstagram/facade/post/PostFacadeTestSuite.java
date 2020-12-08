package com.myinstagram.facade.post;

import com.myinstagram.domain.dto.PostDto;
import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.SimplePostRequest;
import com.myinstagram.domain.dto.UpdatePostRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.post.PostNotFoundException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
import com.myinstagram.service.PostServiceDb;
import com.myinstagram.service.UserServiceDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

import static com.myinstagram.domain.util.Constants.CREATE_POST_IMAGE_SUCCESS;
import static com.myinstagram.domain.util.Constants.POST_FOLDER;
import static com.myinstagram.util.EntityDataFixture.createPost;
import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.OK;

@Transactional
@SpringBootTest
public class PostFacadeTestSuite {
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;
    @Autowired
    private PostFacadeUtils postFacadeUtils;

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
    public void shouldGetPosts() {
        //GIVEN
        List<Post> posts = postServiceDb.getAllPosts();
        //WHEN
        ResponseEntity<List<PostDto>> responseEntityPostsDto = postFacade.getPosts();
        //THEN
        assertEquals(3, responseEntityPostsDto.getBody().size());
        assertEquals(OK, responseEntityPostsDto.getStatusCode());
        IntStream.range(0, 3).forEach(integer -> {
            assertEquals(PostDto.class, responseEntityPostsDto.getBody().get(integer).getClass());
            assertEquals(posts.get(integer).getId(), responseEntityPostsDto.getBody().get(integer).getId());
            assertEquals(posts.get(integer).getUser().getId(), responseEntityPostsDto.getBody().get(integer).getUserId());
            assertEquals(posts.get(integer).getUser().getLogin(), responseEntityPostsDto.getBody().get(integer).getLogin());
        });
    }

    @Test
    public void shouldGetPostsByLogin() {
        //WHEN
        ResponseEntity<List<PostDto>> responseEntityPostsDtoByLogin = postFacade.getPostsByLogin("login1");
        //THEN
        assertEquals(1, responseEntityPostsDtoByLogin.getBody().size());
        assertEquals(OK, responseEntityPostsDtoByLogin.getStatusCode());
        assertEquals(PostDto.class, responseEntityPostsDtoByLogin.getBody().get(0).getClass());
        assertEquals("login1", responseEntityPostsDtoByLogin.getBody().get(0).getLogin());
    }

    @Test
    public void shouldGetPostById() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        //WHEN
        ResponseEntity<PostDto> responseEntityPostDto = postFacade.getPostById(post.getId());
        //THEN
        assertEquals(OK, responseEntityPostDto.getStatusCode());
        assertEquals(PostDto.class, responseEntityPostDto.getBody().getClass());
        assertEquals(post.getId(), responseEntityPostDto.getBody().getId());
        assertEquals(post.getUser().getId(), responseEntityPostDto.getBody().getUserId());
        assertEquals(post.getUser().getLogin(), responseEntityPostDto.getBody().getLogin());
    }

    @Test
    public void shouldNotGetPostByIdAndThrowPostNotFoundException() {
        //WHEN & THEN
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                                                                   () -> postFacade.getPostById(123L));
        assertEquals("Could not find post by id: 123", postNotFoundException.getMessage());
    }

    @Test
    public void shouldPublishPost() {
        //GIVEN
        PostRequest postRequest = createPostRequest("Test Post", "Test Caption").toBuilder().login("login1").build();
        //WHEN
        ResponseEntity<PostDto> postDtoResponseEntity = postFacade.publishPost(postRequest);
        //THEN
        assertEquals(OK, postDtoResponseEntity.getStatusCode());
        assertEquals(PostDto.class, postDtoResponseEntity.getBody().getClass());
        assertEquals(postRequest.getLogin(), postDtoResponseEntity.getBody().getLogin());
        assertEquals(postRequest.getPostName(), postDtoResponseEntity.getBody().getPostName());
        assertEquals(postRequest.getCaption(), postDtoResponseEntity.getBody().getCaption());
        assertEquals(postRequest.getUrl(), postDtoResponseEntity.getBody().getUrl());
        assertEquals(0, postDtoResponseEntity.getBody().getLikesCount());
        assertEquals(Instant.now().truncatedTo(SECONDS), postDtoResponseEntity.getBody().getPostDate().truncatedTo(SECONDS));
        assertEquals(postDtoResponseEntity.getBody().getImageSerialNumber(), postDtoResponseEntity.getBody().getUserId());
    }

    @Test
    public void shouldNotPublishPostAndThrowUserNotFoundException() {
        //GIVEN
        PostRequest postRequest = createPostRequest("Test Post", "Test Caption");
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> postFacade.publishPost(postRequest));
        //THEN
        assertEquals("Could not find user by: Test login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldUploadPostImage() throws IOException {
        //GIVEN
        MockMultipartFile multipartFile = new MockMultipartFile("Test file", new byte[0]);
        //WHEN
        ResponseEntity<String> responseEntity = postFacade.uploadPostImage(multipartFile, "testFile");
        //THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(CREATE_POST_IMAGE_SUCCESS, responseEntity.getBody());
        //CLEANUP
        Files.deleteIfExists(Paths.get(POST_FOLDER + "/testFile.png"));
    }

    @Test
    public void shouldUpdatePost() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        UpdatePostRequest updatePostRequest = createUpdatePostRequest().toBuilder().postId(post.getId()).build();
        //WHEN
        ResponseEntity<PostDto> updatePostDtoResponseEntity = postFacade.updatePost(updatePostRequest);
        //THEN
        assertEquals(OK, updatePostDtoResponseEntity.getStatusCode());
        assertEquals(PostDto.class, updatePostDtoResponseEntity.getBody().getClass());
        assertEquals(updatePostRequest.getPostName(), updatePostDtoResponseEntity.getBody().getPostName());
        assertEquals(updatePostRequest.getCaption(), updatePostDtoResponseEntity.getBody().getCaption());
        assertEquals(Instant.now().truncatedTo(SECONDS), updatePostDtoResponseEntity.getBody().getUpdateDate().truncatedTo(SECONDS));
        assertEquals(post.getUser().getLogin(), updatePostDtoResponseEntity.getBody().getLogin());
        assertEquals(post.getUser().getId(), updatePostDtoResponseEntity.getBody().getUserId());
    }

    @Test
    public void shouldNotUpdatePostAndThrowPostNotFoundException() {
        //GIVEN
        UpdatePostRequest updatePostRequest = createUpdatePostRequest().toBuilder().postId(123L).build();
        //WHEN
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                                                                   () -> postFacade.updatePost(updatePostRequest));
        //THEN
        assertEquals("Could not find post by id: 123", postNotFoundException.getMessage());
    }

    @Test
    public void shouldLikePost() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        Post post = postServiceDb.getAllPosts().get(0);
        SimplePostRequest simplePostRequest = createSimplePostRequest().toBuilder().postId(post.getId()).login(user.getLogin()).build();
        //WHEN
        ResponseEntity<PostDto> postDtoResponseEntity = postFacade.likePost(simplePostRequest);
        //THEN
        assertEquals(OK, postDtoResponseEntity.getStatusCode());
        assertEquals(PostDto.class, postDtoResponseEntity.getBody().getClass());
        assertEquals(1, postDtoResponseEntity.getBody().getLikesCount());
    }

    @Test
    public void shouldNotLikePostAndThrowPostNotFoundException() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        SimplePostRequest simplePostRequest = createSimplePostRequest().toBuilder().postId(123L).login(user.getLogin()).build();
        //WHEN
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                                                                   () -> postFacade.likePost(simplePostRequest));
        //THEN
        assertEquals("Could not find post by id: 123", postNotFoundException.getMessage());
    }

    @Test
    public void shouldNotLikePostAndThrowUserNotFoundException() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        SimplePostRequest simplePostRequest = createSimplePostRequest().toBuilder().postId(post.getId()).login("login").build();
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> postFacade.likePost(simplePostRequest));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldUnlikePost() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        Post post = postServiceDb.getAllPosts().get(0);
        SimplePostRequest simplePostRequest = createSimplePostRequest().toBuilder().postId(post.getId()).login(user.getLogin()).build();
        postFacade.likePost(simplePostRequest);
        //WHEN
        ResponseEntity<PostDto> postDtoResponseEntity = postFacade.unlikePost(simplePostRequest);
        //THEN
        assertEquals(OK, postDtoResponseEntity.getStatusCode());
        assertEquals(PostDto.class, postDtoResponseEntity.getBody().getClass());
        assertEquals(0, postDtoResponseEntity.getBody().getLikesCount());
    }

    @Test
    public void shouldNotUnlikePostAndThrowPostNotFoundException() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        SimplePostRequest simplePostRequest = createSimplePostRequest().toBuilder().postId(123L).login(user.getLogin()).build();
        //WHEN
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                                                                   () -> postFacade.unlikePost(simplePostRequest));
        //THEN
        assertEquals("Could not find post by id: 123", postNotFoundException.getMessage());
    }

    @Test
    public void shouldNotUnlikePostAndThrowUserNotFoundException() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        SimplePostRequest simplePostRequest = createSimplePostRequest().toBuilder().postId(post.getId()).login("login").build();
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> postFacade.unlikePost(simplePostRequest));
        //THEN
        assertEquals("Could not find user by: login", userNotFoundException.getMessage());
    }

    @Test
    public void shouldDeletePostById() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        //WHEN
        ResponseEntity<String> responseEntity = postFacade.deletePostById(post.getId());
        //THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(2, postServiceDb.getAllPosts().size());
    }
}
