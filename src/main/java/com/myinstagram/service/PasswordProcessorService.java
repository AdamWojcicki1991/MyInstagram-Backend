package com.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.myinstagram.domain.util.Constants.CREATE_PASSWORD_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@Service
public final class PasswordProcessorService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String generateRandomPassword() {
        log.info(CREATE_PASSWORD_SUCCESS);
        return UUID.randomUUID().toString();
    }

    public String encryptPassword(final String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
