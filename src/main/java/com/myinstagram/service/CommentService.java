package com.myinstagram.service;

import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.dto.UpdateCommentRequest;
import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final CommentServiceDb commentServiceDb;

    public Comment createComment(final Post post, final CommentRequest commentRequest) {
        return commentServiceDb.saveComment(Comment.builder()
                                                    .commentName(commentRequest.getLogin())
                                                    .content(commentRequest.getContent())
                                                    .commentDate(Instant.now())
                                                    .post(post)
                                                    .build());
    }

    public Comment updateComment(final Comment comment, final UpdateCommentRequest updateCommentRequest) {
        return commentServiceDb.saveComment(comment.toBuilder()
                                                    .commentName(updateCommentRequest.getCommentName())
                                                    .content(updateCommentRequest.getContent())
                                                    .updateDate(Instant.now())
                                                    .build());
    }
}
