package com.myinstagram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.myinstagram.domain.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordProcessorService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String generateUuid() {
        log.info(CREATE_UUID_SUCCESS);
        return UUID.randomUUID().toString();
    }

    public String encryptPassword(final String password) {
        log.info(ENCRYPT_PASSWORD_SUCCESS);
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean isEncryptedPasswordMatching(final String currentPassword, final String userPassword) {
        log.info(ENCRYPT_PASSWORD_MATCHES);
        return bCryptPasswordEncoder.matches(currentPassword, userPassword);
    }
}
