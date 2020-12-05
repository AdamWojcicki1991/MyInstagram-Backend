package com.myinstagram.service;

import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.exceptions.custom.security.InvalidRefreshTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        assertTrue(generateRefreshToken.getToken().matches(
                "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));
        assertEquals(generateRefreshToken.getCreatedDate().truncatedTo(ChronoUnit.SECONDS),
                     Instant.now().truncatedTo(ChronoUnit.SECONDS));
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
