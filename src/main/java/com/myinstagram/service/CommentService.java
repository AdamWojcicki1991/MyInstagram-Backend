package com.myinstagram.service;

import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final CommentServiceDb commentServiceDb;

    public Comment createComment(final Post post, final String commentName, final String content) {
        return commentServiceDb.saveComment(Comment.builder()
                                                    .commentName(commentName)
                                                    .content(content)
                                                    .commentDate(LocalDate.now())
                                                    .post(post)
                                                    .build());
    }
}
