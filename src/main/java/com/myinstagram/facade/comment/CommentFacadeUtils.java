package com.myinstagram.facade.comment;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.entity.Post;
import com.myinstagram.domain.entity.User;
import com.myinstagram.exceptions.custom.comment.CommentNotFoundException;
import com.myinstagram.exceptions.custom.user.UserValidationException;
import com.myinstagram.mapper.CommentMapper;
import com.myinstagram.service.CommentService;
import com.myinstagram.service.CommentServiceDb;
import com.myinstagram.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.myinstagram.domain.enums.ValidationStatus.AUTHORIZED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Component
class CommentFacadeUtils {
    private final UserValidator userValidator;
    private final CommentMapper commentMapper;
    private final CommentService commentService;
    private final CommentServiceDb commentServiceDb;

    ResponseEntity<CommentDto> createCommentIfUserIsAuthorized(final CommentRequest commentRequest, final Post post, final User user) {
        if (userValidator.isUserValidated(user)) {
            CommentDto commentDto = commentMapper.mapToCommentDto(
                    commentService.createComment(post, commentRequest));
            log.info("Comment created successfully!");
            return new ResponseEntity<>(commentDto, OK);
        } else {
            throw new UserValidationException(commentRequest.getLogin(), AUTHORIZED);
        }
    }

    ResponseEntity<String> deleteCommentIfExists(final Long id) {
        try {
            commentServiceDb.deleteCommentById(id);
            log.info("Comment deleted successfully!");
        } catch (EmptyResultDataAccessException e) {
            throw new CommentNotFoundException(id);
        }
        return new ResponseEntity<>("Comment Deleted Successfully!!!", OK);
    }
}
