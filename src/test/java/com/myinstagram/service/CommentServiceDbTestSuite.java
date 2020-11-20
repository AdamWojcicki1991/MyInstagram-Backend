package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.myinstagram.util.TestDataFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Transactional
@SpringBootTest
public class CommentServiceDbTestSuite {
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;
    @Autowired
    private CommentServiceDb commentServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        Post post = postServiceDb.savePost(
                createPost(userServiceDb.saveUser(
                        createUser("login", "email@gmail.com")), LocalDate.now()));
        commentServiceDb.saveComment(createComment(post));
    }

    @Test
    public void shouldGetAllComments() {
        // WHEN
        List<Comment> comments = commentServiceDb.getAllComments();
        // THEN
        assertEquals(1, comments.size());
    }

    @Test
    public void shouldGetCommentById() {
        // GIVEN
        Comment comment = commentServiceDb.getAllComments().get(0);
        // WHEN
        Comment saveComment = commentServiceDb.getCommentById(comment.getId()).get();
        // THEN
        assertEquals(comment, saveComment);
    }

    @Test
    public void shouldSaveComment() {
        // GIVEN
        Comment comment = createComment(postServiceDb.getAllPosts().get(0));
        // WHEN
        Comment saveComment = commentServiceDb.saveComment(comment);
        // THEN
        assertEquals(2, commentServiceDb.getAllComments().size());
        assertNotEquals(0, saveComment.getId());
    }

    @Test
    public void shouldDeleteCommentById() {
        //GIVEN
        Long commentId = commentServiceDb.getAllComments().get(0).getId();
        //WHEN
        commentServiceDb.deleteCommentById(commentId);
        //THEN
        assertEquals(0, commentServiceDb.getAllComments().size());
        assertEquals(Optional.empty(), commentServiceDb.getCommentById(commentId));
    }
}
