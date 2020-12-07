package com.myinstagram.service;

import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.UpdatePostRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static com.myinstagram.util.EntityDataFixture.createUser;
import static com.myinstagram.util.RequestDataFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class PostServiceTestSuite {
    @Autowired
    private PostService postService;
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;

    @Test
    public void shouldCreatePost() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login", "email@gmail.com"));
        PostRequest postRequest = createPostRequest("Test Post", "Test caption");
        //WHEN
        Post post = postService.createPost(user, postRequest);
        //THEN
        assertEquals("Test Post", post.getPostName());
        assertEquals("Test caption", post.getCaption());
        assertEquals("Test url", post.getUrl());
        assertEquals(user, post.getUser());
        assertEquals(1, postServiceDb.getAllPosts().size());
    }

    @Test
    public void shouldUpdatePost() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login", "email@gmail.com"));
        PostRequest postRequest = createPostRequest("Post", "Caption");
        UpdatePostRequest updatePostRequest = createUpdatePostRequest();
        Post post = postService.createPost(user, postRequest);
        //WHEN
        Post updatePost = postService.updatePost(post, updatePostRequest);
        //THEN
        assertEquals(post, updatePost);
    }
}
