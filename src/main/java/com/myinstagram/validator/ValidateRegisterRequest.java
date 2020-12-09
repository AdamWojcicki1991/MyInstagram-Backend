package com.myinstagram.validator;

import com.myinstagram.domain.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidateRegisterRequest {

    public boolean isRegisterRequestValid(final RegisterRequest registerRequest) {
        log.info("Start to validate register: " + registerRequest);
        return isValid(registerRequest.getLogin()) &&
                isValid(registerRequest.getPassword()) &&
                isValid(registerRequest.getEmail()) &&
                isValid(registerRequest.getCity());
    }

    private boolean isValid(final String field) {
        log.info("Validate: " + field);
        return field != null && !field.isBlank();
    }
}
