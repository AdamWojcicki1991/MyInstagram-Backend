package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.HashMap;

import static com.myinstagram.util.DataFixture.createUser;
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
        HashMap<String, String> request = new HashMap<>();
        request.put("caption", "Test caption");
        request.put("url", "Test url");
        //WHEN
        Post post = postService.createPost(user, request, "Test Post");
        //THEN
        assertEquals("Test Post", post.getPostName());
        assertEquals("Test caption", post.getCaption());
        assertEquals("Test url", post.getUrl());
        assertEquals(user, post.getUser());
        assertEquals(1, postServiceDb.getAllPosts().size());
    }
}
