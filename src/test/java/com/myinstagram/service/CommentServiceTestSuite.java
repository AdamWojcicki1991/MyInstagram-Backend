package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.myinstagram.util.DataFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class CommentServiceTestSuite {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;
    @Autowired
    private CommentServiceDb commentServiceDb;

    @Test
    public void shouldCreateComment() {
        //GIVEN
        Post post = postServiceDb.savePost(
                createPost(userServiceDb.saveUser(
                        createUser("login", "email@gmail.com")), LocalDate.now()));
        //WHEN
        Comment createComment = commentService.createComment(post, "Test Comment", "Test Content");
        //THEN
        assertEquals("Test Comment", createComment.getCommentName());
        assertEquals("Test Content", createComment.getContent());
        assertEquals(LocalDate.now(), createComment.getCommentDate());
        assertEquals(post, createComment.getPost());
        assertEquals(1, commentServiceDb.getAllComments().size());
    }
}
