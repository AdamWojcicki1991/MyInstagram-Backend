package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.repository.CommentDbRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class CommentServiceDb {
    private final CommentDbRepository commentDbRepository;

    public List<Comment> getAllComments() {
        return commentDbRepository.findAll();
    }

    public Optional<Comment> getCommentById(final Long id) {
        return commentDbRepository.findById(id);
    }

    public Comment saveComment(final Comment comment) {
        return commentDbRepository.save(comment);
    }

    public void deleteCommentById(final Long id) {
        commentDbRepository.deleteById(id);
    }
}
