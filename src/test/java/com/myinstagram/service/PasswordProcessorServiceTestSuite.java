package com.myinstagram.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordProcessorServiceTestSuite {
    @Autowired
    private PasswordProcessorService passwordProcessorService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void shouldGenerateUuid() {
        // WHEN
        String randomPassword = passwordProcessorService.generateUuid();
        // THEN
        assertNotNull(randomPassword);
        assertTrue(randomPassword.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));
    }

    @Test
    public void shouldEncryptPassword() {
        // GIVEN
        String randomPassword = passwordProcessorService.generateUuid();
        // WHEN
        String encryptedPassword = passwordProcessorService.encryptPassword(randomPassword);
        // THEN
        assertTrue(bCryptPasswordEncoder.matches(randomPassword, encryptedPassword));
    }
}
