package com.myinstagram.facade.comment;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.CommentNotFoundException;
import com.myinstagram.exceptions.custom.PostNotFoundException;
import com.myinstagram.exceptions.custom.UserNotFoundException;
import com.myinstagram.mapper.CommentMapper;
import com.myinstagram.service.CommentServiceDb;
import com.myinstagram.service.PostServiceDb;
import com.myinstagram.service.UserServiceDb;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentFacade {
    private final UserServiceDb userServiceDb;
    private final PostServiceDb postServiceDb;
    private final CommentMapper commentMapper;
    private final CommentServiceDb commentServiceDb;
    private final CommentFacadeUtils commentFacadeUtils;

    public ResponseEntity<List<CommentDto>> getComments() {
        log.info("Get published comments!");
        List<CommentDto> comments = commentMapper.mapToCommentsDto(commentServiceDb.getAllComments());
        return new ResponseEntity<>(comments, OK);
    }

    public ResponseEntity<CommentDto> getComment(final Long id) {
        log.info("Get published comment by id: " + id);
        CommentDto commentDto = commentMapper.mapToCommentDto(commentServiceDb.getCommentById(id)
                                                                      .orElseThrow(() -> new CommentNotFoundException(id)));
        return new ResponseEntity<>(commentDto, OK);
    }

    public ResponseEntity<CommentDto> publishComment(@RequestBody final CommentRequest commentRequest) {
        log.info("Try to publish comment!");
        Post post = postServiceDb.getPostById(commentRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentRequest.getPostId()));
        User user = userServiceDb.getUserByLogin(commentRequest.getLogin())
                .orElseThrow(() -> new UserNotFoundException(commentRequest.getLogin()));
        return commentFacadeUtils.createCommentIfUserIsAuthorized(commentRequest, post, user);
    }

    public ResponseEntity<String> deleteCommentById(final Long id) {
        log.info("Delete published comment by id: " + id);
        return commentFacadeUtils.deleteCommentIfExists(id);
    }
}
