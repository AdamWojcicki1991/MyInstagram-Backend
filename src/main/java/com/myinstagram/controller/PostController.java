package com.myinstagram.controller;

import com.myinstagram.domain.dto.PostDto;
import com.myinstagram.domain.dto.PostRequest;
import com.myinstagram.domain.dto.SimplePostRequest;
import com.myinstagram.facade.PostFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/posts")
public final class PostController {
    private final PostFacade postFacade;

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts() {
        return postFacade.getPosts();
    }

    @GetMapping("/{login}")
    public ResponseEntity<List<PostDto>> getPostsByLogin(@PathVariable final String login) {
        return postFacade.getPostsByLogin(login);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable final Long id) {
        return postFacade.getPostById(id);
    }

    @PostMapping
    public ResponseEntity<PostDto> publishPost(@RequestBody final PostRequest postRequest) {
        return postFacade.publishPost(postRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable final Long id) {
        return postFacade.deletePostById(id);
    }

    @PostMapping("/upload/{postImageName}")
    public ResponseEntity<String> uploadPostImage(@RequestParam("image") final MultipartFile image, @PathVariable final String postImageName) {
        return postFacade.uploadPostImage(image, postImageName);
    }

    @PostMapping("/like")
    public ResponseEntity<PostDto> likePost(@RequestBody final SimplePostRequest simplePostRequest) {
        return postFacade.likePost(simplePostRequest);
    }

    @PostMapping("/unlike")
    public ResponseEntity<PostDto> unlikePost(@RequestBody final SimplePostRequest simplePostRequest) {
        return postFacade.unlikePost(simplePostRequest);
    }
}
