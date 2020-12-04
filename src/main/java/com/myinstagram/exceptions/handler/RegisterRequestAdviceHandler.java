package com.myinstagram.exceptions.handler;

import com.myinstagram.exceptions.custom.register.RegisterRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public final class RegisterRequestAdviceHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RegisterRequestException.class)
    public ResponseEntity<Object> userNotFoundHandler(final RegisterRequestException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, BAD_REQUEST, webRequest);
    }
}
