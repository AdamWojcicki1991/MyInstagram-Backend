package com.myinstagram.facade.post;

import com.myinstagram.domain.dto.PostDto;
import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.SimplePostRequest;
import com.myinstagram.domain.dto.UpdatePostRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.post.PostNotFoundByLoginException;
import com.myinstagram.exceptions.custom.post.PostNotFoundException;
import com.myinstagram.exceptions.custom.user.UserValidationException;
import com.myinstagram.mapper.PostMapper;
import com.myinstagram.service.PostService;
import com.myinstagram.service.PostServiceDb;
import com.myinstagram.service.UserServiceDb;
import com.myinstagram.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

import static com.myinstagram.domain.enums.ValidationStatus.*;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
class PostFacadeUtils {
    private final PostMapper postMapper;
    private final PostService postService;
    private final PostServiceDb postServiceDb;
    private final UserServiceDb userServiceDb;
    private final UserValidator userValidator;

    ResponseEntity<List<PostDto>> getPostsIfExists(final String login) {
        try {
            List<PostDto> posts = postMapper.mapToPostsDto(postServiceDb.getAllPostsByLoginSortedDescending(login));
            log.info("Published posts returned successfully!");
            return new ResponseEntity<>(posts, OK);
        } catch (Exception e) {
            throw new PostNotFoundByLoginException(login);
        }
    }

    ResponseEntity<PostDto> createPostIfUserIsAuthorized(final PostRequest postRequest, final User user) {
        if (userValidator.isUserValidated(user)) {
            PostDto postDto = postMapper.mapToPostDto(postService.createPost(user, postRequest));
            log.info("Published post returned successfully!");
            return new ResponseEntity<>(postDto, OK);
        } else {
            throw new UserValidationException(postRequest.getLogin(), AUTHORIZED);
        }
    }

    public ResponseEntity<PostDto> updatePostIfExist(final Post post, final UpdatePostRequest updatePostRequest) {
        PostDto postDto = postMapper.mapToPostDto(postService.updatePost(post, updatePostRequest));
        log.info("Post updated successfully!");
        return new ResponseEntity<>(postDto, OK);
    }

    ResponseEntity<PostDto> likePostIfUserIsValidated(final SimplePostRequest simplePostRequest, final Post post, final User user) {
        if (userValidator.isUserValidatedToLikePost(user, post)) {
            post.countUp();
            user.getLikedPosts().add(post);
            userServiceDb.saveUser(user);
            log.info("Post liked successfully!");
            return new ResponseEntity<>(postMapper.mapToPostDto(post), OK);
        } else {
            throw new UserValidationException(simplePostRequest.getLogin(), AUTHORIZED_CONTAINS_POST_LIKED);
        }
    }

    ResponseEntity<PostDto> unlikePostIfUserIsValidated(final SimplePostRequest simplePostRequest, final Post post, final User user) {
        if (userValidator.isUserValidatedToUnlikePost(user, post)) {
            post.countDown();
            user.getLikedPosts().remove(post);
            userServiceDb.saveUser(user);
            log.info("Post unliked successfully!");
            return new ResponseEntity<>(postMapper.mapToPostDto(post), OK);
        } else {
            throw new UserValidationException(simplePostRequest.getLogin(), AUTHORIZED_CONTAINS_POST_UNLIKED);
        }
    }

    ResponseEntity<String> deletePostIfExists(final Long id) {
        try {
            postServiceDb.deletePostById(id);
            log.info("Post deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            throw new PostNotFoundException(id);
        }
        return new ResponseEntity<>("Post Deleted Successfully!", OK);
    }
}
