package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class PostServiceDb {
    private final PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getAllPostsSorted() {
        return postRepository.findAllSorted();
    }

    public List<Post> getAllPostsByLoginSorted(final String login) {
        return postRepository.findAllByLoginSorted(login);
    }

    public Optional<Post> getPostById(final Long id) {
        return postRepository.findById(id);
    }

    public Post savePost(final Post post) {
        return postRepository.save(post);
    }

    public void deletePostById(final Long id) {
        postRepository.deleteById(id);
    }
}
