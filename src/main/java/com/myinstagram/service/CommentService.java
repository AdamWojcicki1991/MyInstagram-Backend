package com.myinstagram.service;

import com.myinstagram.domain.entity.Post;
import com.myinstagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService implements CommentRepository {

    @Override
    public void createComment(Post post, String login, String content) {

    }
}
