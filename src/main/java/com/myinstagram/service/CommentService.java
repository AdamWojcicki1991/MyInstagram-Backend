package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService implements CommentRepository {
    private final CommentServiceDb commentServiceDb;

    @Override
    public void createComment(Post post, String commentName, String content) {
        commentServiceDb.saveComment(Comment.builder()
                                             .commentName(commentName)
                                             .content(content)
                                             .commentDate(LocalDate.now())
                                             .post(post)
                                             .build());
    }
}
