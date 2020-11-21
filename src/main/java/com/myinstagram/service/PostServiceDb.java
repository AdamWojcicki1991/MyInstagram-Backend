package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.repository.PostDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class PostServiceDb {
    private final PostDbRepository postDbRepository;

    public List<Post> getAllPosts() {
        return postDbRepository.findAll();
    }

    public List<Post> getAllPostsSortedDescending() {
        return postDbRepository.findAllSortedDescending();
    }

    public List<Post> getAllPostsByLoginSortedDescending(final String login) {
        return postDbRepository.findAllByLoginSortedDescending(login);
    }

    public Optional<Post> getPostById(final Long id) {
        return postDbRepository.findById(id);
    }

    public Post savePost(final Post post) {
        return postDbRepository.save(post);
    }

    public void deletePostById(final Long id) {
        postDbRepository.deleteById(id);
    }
}
