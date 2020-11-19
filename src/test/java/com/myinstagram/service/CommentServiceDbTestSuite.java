package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
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
        commentServiceDb.saveComment(setUpData());
        // WHEN
        List<Comment> comments = commentServiceDb.getAllComments();
        // THEN
        assertEquals(1, comments.size());
    }

    @Test
    public void shouldGetCommentById() {
        // GIVEN
        Comment comment = commentServiceDb.saveComment(setUpData());
        // WHEN
        Comment saveComment = commentServiceDb.getCommentById(comment.getId()).get();
        // THEN
        assertEquals(comment, saveComment);
    }

    @Test
    public void shouldSaveComment() {
        // GIVEN
        Comment comment = setUpData();
        // WHEN
        Comment saveComment = commentServiceDb.saveComment(comment);
        // THEN
        assertNotEquals(0, saveComment.getId());
    }

    @Test
    public void shouldDeleteCommentById() {
        //GIVEN
        Long commentId = commentServiceDb.saveComment(setUpData()).getId();
        //WHEN
        commentServiceDb.deleteCommentById(commentId);
        //THEN
        assertEquals(Optional.empty(), commentServiceDb.getCommentById(commentId));
    }

    private Comment setUpData() {
        User user = createUser();
        Post post = createPost(user);
        userServiceDb.saveUser(user);
        postServiceDb.savePost(post);
        return createComment(post);
    }
}
