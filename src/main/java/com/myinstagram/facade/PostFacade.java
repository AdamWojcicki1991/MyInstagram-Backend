package com.myinstagram.facade;

import com.myinstagram.domain.dto.PostDto;
import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.SimplePostRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.PostNotFoundByLoginException;
import com.myinstagram.exceptions.PostNotFoundException;
import com.myinstagram.exceptions.UserNotFoundException;
import com.myinstagram.exceptions.UserValidationException;
import com.myinstagram.mapper.PostMapper;
import com.myinstagram.service.ImageService;
import com.myinstagram.service.PostService;
import com.myinstagram.service.PostServiceDb;
import com.myinstagram.service.UserServiceDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;
import static com.myinstagram.domain.util.Constants.CREATE_POST_IMAGE_SUCCESS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostFacade {
    private final PostMapper postMapper;
    private final PostService postService;
    private final ImageService imageService;
    private final UserServiceDb userServiceDb;
    private final PostServiceDb postServiceDb;

    public ResponseEntity<List<PostDto>> getPosts() {
        log.info("Get published posts!");
        List<PostDto> posts = postMapper.mapToPostsDto(postServiceDb.getAllPosts());
        return new ResponseEntity<>(posts, OK);
    }

    public ResponseEntity<List<PostDto>> getPostsByLogin(final String login) {
        log.info("Get published posts by login sorted descending!");
        try {
            List<PostDto> posts = postMapper.mapToPostsDto(postServiceDb.getAllPostsByLoginSortedDescending(login));
            return new ResponseEntity<>(posts, OK);
        } catch (Exception e) {
            throw new PostNotFoundByLoginException(login);
        }
    }

    public ResponseEntity<PostDto> getPostById(final Long id) {
        log.info("Get published post by id: " + id);
        PostDto postDto = postMapper.mapToPostDto(postServiceDb.getPostById(id)
                                                          .orElseThrow(() -> new PostNotFoundException(id)));
        return new ResponseEntity<>(postDto, OK);
    }

    public ResponseEntity<PostDto> publishPost(final PostRequest postRequest) {
        log.info("Try to publish post!");
        User user = userServiceDb.getUserByLogin(postRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(postRequest.getLogin()));
        if (user.isEnabled() && user.getUserStatus().equals(ACTIVE)) {
            PostDto postDto = postMapper.mapToPostDto(postService.createPost(user, postRequest));
            return new ResponseEntity<>(postDto, OK);
        } else {
            throw new UserValidationException(postRequest.getLogin());
        }
    }

    public ResponseEntity<String> deletePostById(final Long id) {
        log.info("Delete published post by id: " + id);
        try {
            postServiceDb.deletePostById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new PostNotFoundException(id);
        }
        return new ResponseEntity<>("Post Deleted Successfully!!!", OK);
    }

    public ResponseEntity<String> uploadPostImage(final MultipartFile image, final String postImageName) {
        log.info("Upload post image with name: " + postImageName);
        String result = imageService.loadPostImage(image, postImageName);
        return (result.equals(CREATE_POST_IMAGE_SUCCESS)) ?
                new ResponseEntity<>(result, OK) :
                new ResponseEntity<>(result, BAD_REQUEST);
    }

    public ResponseEntity<PostDto> likePost(final SimplePostRequest simplePostRequest) {
        log.info("Try to like post!");
        Post post = postServiceDb.getPostById(simplePostRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(simplePostRequest.getPostId()));
        User user = userServiceDb.getUserByLogin(simplePostRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(simplePostRequest.getLogin()));
        if (user.isEnabled() && user.getUserStatus().equals(ACTIVE) && !user.getLikedPosts().contains(post)) {
            post.countUp();
            user.getLikedPosts().add(post);
            userServiceDb.saveUser(user);
            return new ResponseEntity<>(postMapper.mapToPostDto(post), OK);
        } else {
            throw new UserValidationException(simplePostRequest.getLogin());
        }
    }

    public ResponseEntity<PostDto> unlikePost(final SimplePostRequest simplePostRequest) {
        log.info("Try to unlike post!");
        Post post = postServiceDb.getPostById(simplePostRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(simplePostRequest.getPostId()));
        User user = userServiceDb.getUserByLogin(simplePostRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(simplePostRequest.getLogin()));
        if (user.isEnabled() && user.getUserStatus().equals(ACTIVE) && user.getLikedPosts().contains(post)) {
            post.countDown();
            user.getLikedPosts().remove(post);
            userServiceDb.saveUser(user);
            return new ResponseEntity<>(postMapper.mapToPostDto(post), OK);
        } else {
            throw new UserValidationException(simplePostRequest.getLogin());
        }
    }
}
