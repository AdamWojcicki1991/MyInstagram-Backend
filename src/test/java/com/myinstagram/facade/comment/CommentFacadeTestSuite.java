package com.myinstagram.facade.comment;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.dto.UpdateCommentRequest;
import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.comment.CommentNotFoundException;
import com.myinstagram.exceptions.custom.post.PostNotFoundException;
import com.myinstagram.exceptions.custom.user.UserNotFoundException;
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
import java.util.List;
import java.util.stream.IntStream;

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
public class CommentFacadeTestSuite {
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;
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
    public void shouldGetComments() {
        //GIVEN
        List<Comment> comments = commentServiceDb.getAllComments();
        //WHEN
        ResponseEntity<List<CommentDto>> commentsDtoResponseEntity = commentFacade.getComments();
        //THEN
        assertEquals(3, commentsDtoResponseEntity.getBody().size());
        assertEquals(OK, commentsDtoResponseEntity.getStatusCode());
        IntStream.range(0, 3).forEach(integer -> {
            assertEquals(CommentDto.class, commentsDtoResponseEntity.getBody().get(integer).getClass());
            assertEquals(comments.get(integer).getId(), commentsDtoResponseEntity.getBody().get(integer).getId());
            assertEquals(comments.get(integer).getPost().getId(), commentsDtoResponseEntity.getBody().get(integer).getPostId());
            assertEquals(comments.get(integer).getPost().getUser().getId(), commentsDtoResponseEntity.getBody().get(integer).getUserId());
        });
    }

    @Test
    public void shouldGetComment() {
        //GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0);
        //WHEN
        ResponseEntity<CommentDto> commentDtoResponseEntity = commentFacade.getComment(comment.getId());
        //THEN
        assertEquals(OK, commentDtoResponseEntity.getStatusCode());
        assertEquals(CommentDto.class, commentDtoResponseEntity.getBody().getClass());
        assertEquals(comment.getId(), commentDtoResponseEntity.getBody().getId());
        assertEquals(comment.getPost().getId(), commentDtoResponseEntity.getBody().getPostId());
        assertEquals(comment.getPost().getUser().getId(), commentDtoResponseEntity.getBody().getUserId());
    }

    @Test
    public void shouldNotGetCommentAndThrowCommentNotFoundException() {
        //WHEN & THEN
        CommentNotFoundException commentNotFoundException = assertThrows(CommentNotFoundException.class,
                                                                         () -> commentFacade.getComment(123L));
        assertEquals("Could not find comment by id: 123", commentNotFoundException.getMessage());
    }

    @Test
    public void shouldPublishComment() {
        //GIVEN
        User user = userServiceDb.getAllUsers().get(0);
        Post post = postServiceDb.getAllPosts().get(1);
        CommentRequest commentRequest = createCommentRequest().toBuilder().login(user.getLogin()).postId(post.getId()).build();
        //WHEN
        ResponseEntity<CommentDto> commentDtoResponseEntity = commentFacade.publishComment(commentRequest);
        //THEN
        assertEquals(OK, commentDtoResponseEntity.getStatusCode());
        assertEquals(CommentDto.class, commentDtoResponseEntity.getBody().getClass());
        assertEquals(commentRequest.getPostId(), commentDtoResponseEntity.getBody().getPostId());
        assertEquals(commentRequest.getContent(), commentDtoResponseEntity.getBody().getContent());
        assertEquals(commentRequest.getLogin(), commentDtoResponseEntity.getBody().getCommentName());
    }

    @Test
    public void shouldNotPublishCommentAndThrowPostNotFoundException() {
        //GIVEN
        CommentRequest commentRequest = createCommentRequest().toBuilder().postId(123L).build();
        //WHEN
        PostNotFoundException postNotFoundException = assertThrows(PostNotFoundException.class,
                                                                   () -> commentFacade.publishComment(commentRequest));
        //THEN
        assertEquals("Could not find post by id: 123", postNotFoundException.getMessage());
    }

    @Test
    public void shouldNotPublishCommentAndUserNotFoundException() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(1);
        CommentRequest commentRequest = createCommentRequest().toBuilder().postId(post.getId()).build();
        //WHEN
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                                                                   () -> commentFacade.publishComment(commentRequest));
        //THEN
        assertEquals("Could not find user by: Test Comment", userNotFoundException.getMessage());
    }

    @Test
    public void shouldUpdateComment() {
        //GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0);
        UpdateCommentRequest updateCommentRequest = createUpdateCommentRequest(comment);
        //WHEN
        ResponseEntity<CommentDto> commentDtoResponseEntity = commentFacade.updateComment(updateCommentRequest);
        //THEN
        assertEquals(OK, commentDtoResponseEntity.getStatusCode());
        assertEquals(CommentDto.class, commentDtoResponseEntity.getBody().getClass());
        assertEquals("Test Comment", commentDtoResponseEntity.getBody().getCommentName());
        assertEquals("Test Content", commentDtoResponseEntity.getBody().getContent());
        assertEquals(Instant.now().truncatedTo(SECONDS),
                     commentDtoResponseEntity.getBody().getUpdateDate().truncatedTo(SECONDS));
    }

    @Test
    public void shouldNotUpdateCommentAndThrowCommentNotFoundException() {
        //GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0).toBuilder().id(123L).build();
        UpdateCommentRequest updateCommentRequest = createUpdateCommentRequest(comment);
        //WHEN
        CommentNotFoundException commentNotFoundException = assertThrows(CommentNotFoundException.class,
                                                                         () -> commentFacade.updateComment(
                                                                                 updateCommentRequest));
        //THEN
        assertEquals("Could not find comment by id: 123", commentNotFoundException.getMessage());
    }

    @Test
    public void shouldDeleteCommentById() {
        //GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0);
        //WHEN
        ResponseEntity<String> stringResponseEntity = commentFacade.deleteCommentById(comment.getId());
        //THEN
        assertEquals(OK, stringResponseEntity.getStatusCode());
        assertEquals(2, commentServiceDb.getAllComments().size());
    }
}
