package com.myinstagram.service;

import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.exceptions.custom.security.InvalidRefreshTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

import static com.myinstagram.domain.util.Constants.VALID_UUID;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class RefreshTokenServiceTestSuite {
    @Autowired
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    public void setUp() {
        //GIVEN
        refreshTokenService.generateRefreshToken();
        refreshTokenService.generateRefreshToken();
        refreshTokenService.generateRefreshToken();
    }

    @Test
    public void shouldGetRefreshTokens() {
        //WHEN
        List<RefreshToken> refreshTokens = refreshTokenService.getRefreshTokens();
        //THEN
        assertEquals(3, refreshTokens.size());
    }

    @Test
    public void shouldGenerateRefreshToken() {
        //WHEN
        RefreshToken generateRefreshToken = refreshTokenService.generateRefreshToken();
        //THEN
        assertEquals(4, refreshTokenService.getRefreshTokens().size());
        assertNotNull(generateRefreshToken.getId());
        assertTrue(generateRefreshToken.getToken().matches(VALID_UUID));
        assertEquals(generateRefreshToken.getCreatedDate().truncatedTo(SECONDS),
                     Instant.now().truncatedTo(SECONDS));
    }

    @Test
    public void shouldValidateRefreshToken() {
        //GIVEN
        RefreshToken generateRefreshToken = refreshTokenService.generateRefreshToken();
        //WHEN
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(generateRefreshToken.getToken());
        //THEN
        assertNotNull(refreshToken);
        assertNotNull(refreshToken.getId());
    }

    @Test
    public void shouldThrowValidationExceptionByTakingInvalidRefreshToken() {
        //WHEN
        InvalidRefreshTokenException invalidRefreshTokenException = assertThrows(
                InvalidRefreshTokenException.class,
                () -> refreshTokenService.validateRefreshToken("Test invalid"));
        //THEN
        assertEquals("Invalid refresh token passed: Test invalid", invalidRefreshTokenException.getMessage());
    }

    @Test
    public void shouldDeleteRefreshToken() {
        //GIVEN
        RefreshToken generateRefreshToken = refreshTokenService.generateRefreshToken();
        //WHEN
        refreshTokenService.deleteRefreshToken(generateRefreshToken.getToken());
        //THEN
        assertEquals(3, refreshTokenService.getRefreshTokens().size());
    }
}
