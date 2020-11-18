package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(final Long id) {
        return commentRepository.findById(id);
    }

    public Comment saveComment(final Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteCommentById(final Long id) {
        commentRepository.deleteById(id);
    }
}
