package com.myinstagram.exceptions.handler;

import com.myinstagram.exceptions.custom.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

import static org.springframework.http.HttpHeaders.EMPTY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public final class SecurityErrorAdviceHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<Object> verificationTokenNotFoundHandler(final VerificationTokenNotFoundException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }

    @ExceptionHandler(KeyStoreNotLoadException.class)
    public ResponseEntity<Object> keyStoreNotLoadHandler(final KeyStoreNotLoadException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }

    @ExceptionHandler(PublicKeyNotFoundException.class)
    public ResponseEntity<Object> publicKeyNotFoundHandler(final PublicKeyNotFoundException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Object> invalidRefreshTokenHandler(final InvalidRefreshTokenException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, NOT_FOUND, webRequest);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<Object> passwordNotMatchedHandler(final PasswordNotMatchedException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, BAD_REQUEST, webRequest);
    }

    @ExceptionHandler(ChangePasswordException.class)
    public ResponseEntity<Object> changePasswordHandler(final ChangePasswordException e, final WebRequest webRequest) {
        log.error(Arrays.toString(e.getStackTrace()));
        return handleExceptionInternal(e, e.getMessage(), EMPTY, BAD_REQUEST, webRequest);
    }
}
