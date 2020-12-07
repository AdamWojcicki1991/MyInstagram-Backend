package com.myinstagram.service;

import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.dto.UpdateCommentRequest;
import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;

import static com.myinstagram.util.DataFixture.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class CommentServiceTestSuite {
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentServiceDb commentServiceDb;

    @Test
    public void shouldCreateComment() {
        //GIVEN
        Post post = postServiceDb.savePost(
                createPost(userServiceDb.saveUser(
                        createUser("login", "email@gmail.com")), Instant.now()));
        CommentRequest commentRequest = createCommentRequest();
        //WHEN
        Comment createComment = commentService.createComment(post, commentRequest);
        Instant expectedTime = Instant.now().truncatedTo(SECONDS);
        //THEN
        assertEquals("Test Comment", createComment.getCommentName());
        assertEquals("Test Content", createComment.getContent());
        assertEquals(expectedTime, createComment.getCommentDate().truncatedTo(SECONDS));
        assertEquals(post, createComment.getPost());
        assertEquals(1, commentServiceDb.getAllComments().size());
    }

    @Test
    public void shouldUpdateComment() {
        //GIVEN
        Post post = postServiceDb.savePost(
                createPost(userServiceDb.saveUser(
                        createUser("login", "email@gmail.com")), Instant.now()));
        Comment comment = commentServiceDb.saveComment(createComment(post));
        UpdateCommentRequest updateCommentRequest = createUpdateCommentRequest(comment);
        //WHEN
        Comment updateComment = commentService.updateComment(comment, updateCommentRequest);
        //THEN
        assertEquals(comment, updateComment);
    }
}
