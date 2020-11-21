package com.myinstagram.repository;

import com.myinstagram.domain.entity.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {
    void createComment(Post post, String login, String content);
}
