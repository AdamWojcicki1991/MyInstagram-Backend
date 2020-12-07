package com.myinstagram.facade.comment;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.dto.UpdateCommentRequest;
import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.comment.CommentNotFoundException;
import com.myinstagram.exceptions.custom.user.UserValidationException;
import com.myinstagram.service.CommentServiceDb;
import com.myinstagram.service.PostServiceDb;
import com.myinstagram.service.UserServiceDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.time.Instant;

import static com.myinstagram.util.EntityDataFixture.*;
import static com.myinstagram.util.RequestDataFixture.createCommentRequest;
import static com.myinstagram.util.RequestDataFixture.createUpdateCommentRequest;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.OK;

@Transactional
@SpringBootTest
public class CommentFacadeUtilsTestSuite {
    @Autowired
    CommentFacadeUtils commentFacadeUtils;
    @Autowired
    private PostServiceDb postServiceDb;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private CommentServiceDb commentServiceDb;

    @BeforeEach
    public void setUp() {
        commentServiceDb.saveComment(createComment(
                postServiceDb.savePost(createPost(
                        userServiceDb.saveUser(createUser("login1", "test1@gmail.com")),
                        Instant.now()))));
        commentServiceDb.saveComment(createComment(
                postServiceDb.savePost(createPost(
                        userServiceDb.saveUser(createUser("login2", "test2@gmail.com")),
                        Instant.now().plus(1, DAYS)))));
        commentServiceDb.saveComment(createComment(
                postServiceDb.savePost(createPost(
                        userServiceDb.saveUser(createUser("login3", "test3@gmail.com")),
                        Instant.now().plus(2, DAYS)))));
    }

    @Test
    public void shouldCreateCommentIfUserIsAuthorized() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        Post post = postServiceDb.getAllPosts().get(1);
        CommentRequest commentRequest = createCommentRequest().toBuilder().login(user.getLogin()).postId(post.getId()).build();
        //WHEN
        ResponseEntity<CommentDto> commentIfUserIsAuthorized = commentFacadeUtils.createCommentIfUserIsAuthorized
                (commentRequest, post, user);
        //THEN
        assertEquals(OK, commentIfUserIsAuthorized.getStatusCode());
        assertEquals(CommentDto.class, commentIfUserIsAuthorized.getBody().getClass());
        assertEquals(commentRequest.getPostId(), commentIfUserIsAuthorized.getBody().getPostId());
        assertEquals(commentRequest.getContent(), commentIfUserIsAuthorized.getBody().getContent());
        assertEquals(commentRequest.getLogin(), commentIfUserIsAuthorized.getBody().getCommentName());
    }

    @Test
    public void shouldNotCreateCommentIfUserIsNotAuthorized() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0).toBuilder().enabled(false).build();
        Post post = postServiceDb.getAllPosts().get(1);
        CommentRequest commentRequest = createCommentRequest();
        //WHEN
        UserValidationException userValidationException = assertThrows(
                UserValidationException.class,
                () -> commentFacadeUtils.createCommentIfUserIsAuthorized(
                        commentRequest, post, user));
        //THEN
        assertEquals("User Test Comment is not authorized or active!", userValidationException.getMessage());
    }

    @Test
    public void shouldUpdateCommentIfExists() {
        //GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0);
        UpdateCommentRequest updateCommentRequest = createUpdateCommentRequest(comment);
        //WHEN
        ResponseEntity<CommentDto> commentDtoResponseEntity = commentFacadeUtils.updateCommentIfExists(
                comment, updateCommentRequest);
        //THEN
        assertEquals(OK, commentDtoResponseEntity.getStatusCode());
        assertEquals(CommentDto.class, commentDtoResponseEntity.getBody().getClass());
        assertEquals("Test Comment", commentDtoResponseEntity.getBody().getCommentName());
        assertEquals("Test Content", commentDtoResponseEntity.getBody().getContent());
        assertEquals(Instant.now().truncatedTo(SECONDS),
                     commentDtoResponseEntity.getBody().getUpdateDate().truncatedTo(SECONDS));
    }

    @Test
    public void shouldDeleteCommentIfExists() {
        //GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0);
        //WHEN
        ResponseEntity<String> stringResponseEntity = commentFacadeUtils.deleteCommentIfExists(comment.getId());
        //THEN
        assertEquals(OK, stringResponseEntity.getStatusCode());
        assertEquals("Comment Deleted Successfully!!!", stringResponseEntity.getBody());
        assertEquals(2, commentServiceDb.getAllComments().size());
    }

    @Test
    public void shouldNotDeleteCommentIfNotExistsAndThrow() {
        //GIVEN & WHEN
        CommentNotFoundException commentNotFoundException = assertThrows(CommentNotFoundException.class,
                                                                         () -> commentFacadeUtils.deleteCommentIfExists(123L));
        //THEN
        assertEquals("Could not find comment by id: 123", commentNotFoundException.getMessage());
    }
}
