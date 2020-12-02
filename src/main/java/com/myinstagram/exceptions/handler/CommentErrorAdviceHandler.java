package com.myinstagram.exceptions.handler;

import com.myinstagram.exceptions.custom.comment.CommentNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public final class CommentErrorAdviceHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Object> commentNotFoundHandler(final CommentNotFoundException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }
}
