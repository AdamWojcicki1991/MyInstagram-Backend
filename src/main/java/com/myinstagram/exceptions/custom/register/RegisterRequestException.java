package com.myinstagram.exceptions.custom.register;

import com.myinstagram.domain.auth.RegisterRequest;

public class RegisterRequestException extends RuntimeException {
    public RegisterRequestException(RegisterRequest registerRequest) {
        super(registerRequest + " is not valid!");
    }
}
