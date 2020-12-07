package com.myinstagram.service;

import com.google.common.collect.Ordering;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.myinstagram.util.DataFixture.createPost;
import static com.myinstagram.util.DataFixture.createUser;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class PostServiceDbTestSuite {
    @Autowired
    private UserServiceDb userServiceDb;
    @Autowired
    private PostServiceDb postServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        User user = userServiceDb.saveUser(createUser("login_1", "email1@gmail.com"));
        postServiceDb.savePost(createPost(
                userServiceDb.saveUser(user), Instant.now().truncatedTo(SECONDS)));
        postServiceDb.savePost(createPost(
                userServiceDb.saveUser(user),
                Instant.now().truncatedTo(SECONDS).plus(1, DAYS)));
        postServiceDb.savePost(
                createPost(userServiceDb.saveUser(
                        createUser("login_2", "email2@gmail.com")),
                           Instant.now().truncatedTo(SECONDS).plus(2, DAYS)));
        postServiceDb.savePost(
                createPost(userServiceDb.saveUser(
                        createUser("login_3", "email3@gmail.com")),
                           Instant.now().truncatedTo(SECONDS).plus(3, DAYS)));
    }

    @Test
    public void shouldGetAllPosts() {
        //WHEN
        List<Post> posts = postServiceDb.getAllPosts();
        //THEN
        assertEquals(4, posts.size());
    }

    @Test
    public void shouldGetAllPostsSortedDescending() {
        //WHEN
        List<Post> postsSorted = postServiceDb.getAllPostsSortedDescending();
        //THEN
        assertEquals(4, postsSorted.size());
        assertTrue(Ordering.natural().reverse().isOrdered(postsSorted));
    }

    @Test
    public void shouldGetAllPostsByLoginSortedDescending() {
        //WHEN
        List<Post> postsIncludesLoginSorted = postServiceDb.getAllPostsByLoginSortedDescending("login_1");
        //THEN
        assertEquals(2, postsIncludesLoginSorted.size());
        assertTrue(Ordering.natural().reverse().isOrdered(postsIncludesLoginSorted));
    }

    @Test
    public void shouldGetPostById() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        //WHEN
        Post savePost = postServiceDb.getPostById(post.getId()).get();
        //THEN
        assertEquals(post, savePost);
    }

    @Test
    public void shouldSavePost() {
        //GIVEN
        Post post = createPost(userServiceDb.getAllUsers().get(0), Instant.now());
        //WHEN
        Post savePost = postServiceDb.savePost(post);
        //THEN
        assertEquals(5, postServiceDb.getAllPosts().size());
        assertNotEquals(0, savePost.getId());
    }

    @Test
    public void shouldDeletePostById() {
        //GIVEN
        Long postId = postServiceDb.getAllPosts().get(0).getId();
        //WHEN
        postServiceDb.deletePostById(postId);
        //THEN
        assertEquals(3, postServiceDb.getAllPosts().size());
        assertEquals(Optional.empty(), postServiceDb.getPostById(postId));
    }

    @Test
    public void shouldDeletePost() {
        //GIVEN
        Post post = postServiceDb.getAllPosts().get(0);
        //WHEN
        postServiceDb.deletePost(post);
        //THEN
        assertEquals(3, postServiceDb.getAllPosts().size());
        assertEquals(Optional.empty(), postServiceDb.getPostById(post.getId()));
    }
}
