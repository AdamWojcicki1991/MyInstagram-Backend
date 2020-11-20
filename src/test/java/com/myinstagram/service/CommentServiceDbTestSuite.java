package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
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

    @Test
    public void shouldGetAllComments() {
        // GIVEN
        Post post = postServiceDb.savePost(createPost(userServiceDb.saveUser(createUser())));
        commentServiceDb.saveComment(createComment(post));
        // WHEN
        List<Comment> comments = commentServiceDb.getAllComments();
        // THEN
        assertEquals(1, comments.size());
    }

    @Test
    public void shouldGetCommentById() {
        // GIVEN
        Post post = postServiceDb.savePost(createPost(userServiceDb.saveUser(createUser())));
        Comment comment = commentServiceDb.saveComment(createComment(post));
        // WHEN
        Comment saveComment = commentServiceDb.getCommentById(comment.getId()).get();
        // THEN
        assertEquals(comment.getId(), saveComment.getId());
        assertEquals(comment.getCommentName(), saveComment.getCommentName());
        assertEquals(comment.getContent(), saveComment.getContent());
        assertEquals(comment.getCommentDate(), saveComment.getCommentDate());
    }

    @Test
    public void shouldSaveComment() {
        // GIVEN
        Post post = postServiceDb.savePost(createPost(userServiceDb.saveUser(createUser())));
        Comment comment = commentServiceDb.saveComment(createComment(post));
        // WHEN
        Comment saveComment = commentServiceDb.saveComment(comment);
        // THEN
        assertNotEquals(0, saveComment.getId());
    }

    @Test
    public void shouldDeleteCommentById() {
        //GIVEN
        Post post = postServiceDb.savePost(createPost(userServiceDb.saveUser(createUser())));
        Long commentId = commentServiceDb.saveComment(createComment(post)).getId();
        //WHEN
        commentServiceDb.deleteCommentById(commentId);
        //THEN
        assertEquals(Optional.empty(), commentServiceDb.getCommentById(commentId));
    }
}
