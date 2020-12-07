package com.myinstagram.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.myinstagram.domain.util.Constants.VALID_UUID;
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
        //WHEN
        String randomPassword = passwordProcessorService.generateUuid();
        //THEN
        assertNotNull(randomPassword);
        assertTrue(randomPassword.matches(VALID_UUID));
    }

    @Test
    public void shouldEncryptPassword() {
        //GIVEN
        String randomPassword = passwordProcessorService.generateUuid();
        //WHEN
        String encryptedPassword = passwordProcessorService.encryptPassword(randomPassword);
        //THEN
        assertTrue(bCryptPasswordEncoder.matches(randomPassword, encryptedPassword));
    }

    @Test
    public void shouldCheckThatIsEncryptedPasswordMatching() {
        //GIVEN
        String randomPassword = passwordProcessorService.generateUuid();
        String encryptedPassword = passwordProcessorService.encryptPassword(randomPassword);
        //WHEN
        boolean result = passwordProcessorService.isEncryptedPasswordMatching(randomPassword, encryptedPassword);
        //THEN
        assertTrue(result);
    }
}
