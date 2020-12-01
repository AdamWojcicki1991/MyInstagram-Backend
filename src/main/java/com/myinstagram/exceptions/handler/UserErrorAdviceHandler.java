package com.myinstagram.exceptions.handler;

import com.myinstagram.exceptions.custom.UserFoundException;
import com.myinstagram.exceptions.custom.UserNotFoundByIdException;
import com.myinstagram.exceptions.custom.UserNotFoundException;
import com.myinstagram.exceptions.custom.UserValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public final class UserErrorAdviceHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundHandler(final UserNotFoundException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }

    @ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<Object> userNotFoundByIdHandler(final UserNotFoundByIdException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }

    @ExceptionHandler(UserFoundException.class)
    public ResponseEntity<Object> userFoundHandler(final UserFoundException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, FOUND, webRequest);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Object> userValidationHandler(final UserValidationException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, BAD_REQUEST, webRequest);
    }
}
