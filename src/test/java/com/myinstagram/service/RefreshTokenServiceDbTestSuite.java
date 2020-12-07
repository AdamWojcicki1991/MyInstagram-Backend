package com.myinstagram.service;

import com.myinstagram.domain.entity.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

import static com.myinstagram.util.EntityDataFixture.createRefreshToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class RefreshTokenServiceDbTestSuite {
    @Autowired
    private RefreshTokenServiceDb refreshTokenServiceDb;

    @BeforeEach
    public void setUp() {
        //GIVEN
        refreshTokenServiceDb.saveRefreshToken(createRefreshToken("e6eeb33e-3688-11eb-adc1-0242ac120002"));
        refreshTokenServiceDb.saveRefreshToken(createRefreshToken("e6eeb33e-3688-11eb-adc1-0242ac120012"));
        refreshTokenServiceDb.saveRefreshToken(createRefreshToken("e6eeb33e-3688-11eb-adc1-0242ac122342"));
    }

    @Test
    public void shouldGetAllRefreshTokens() {
        //WHEN
        List<RefreshToken> tokens = refreshTokenServiceDb.getAllRefreshTokens();
        //THEN
        assertEquals(3, tokens.size());
    }

    @Test
    public void shouldGetRefreshToken() {
        //WHEN
        RefreshToken refreshTokenFromDb = refreshTokenServiceDb.getRefreshToken("e6eeb33e-3688-11eb-adc1-0242ac120002").get();
        //THEN
        assertNotNull(refreshTokenFromDb);
        assertEquals("e6eeb33e-3688-11eb-adc1-0242ac120002", refreshTokenFromDb.getToken());
    }

    @Test
    public void shouldSaveRefreshToken() {
        //GIVEN
        RefreshToken token = createRefreshToken("e6eeb33e-1234-11eb-adc1-0242ac120002");
        //WHEN
        RefreshToken refreshToken = refreshTokenServiceDb.saveRefreshToken(token);
        //THEN
        assertEquals(4, refreshTokenServiceDb.getAllRefreshTokens().size());
        assertNotNull(refreshToken.getId());
    }

    @Test
    public void shouldDeleteRefreshTokenById() {
        //GIVEN
        RefreshToken token = refreshTokenServiceDb.saveRefreshToken(createRefreshToken("e6eeb33e-3688-11eb-adc1-0242ac120002"));
        //WHEN
        refreshTokenServiceDb.deleteRefreshTokenById(token.getId());
        //THEN
        assertEquals(3, refreshTokenServiceDb.getAllRefreshTokens().size());
    }

    @Test
    public void shouldDeleteRefreshTokenByToken() {
        //GIVEN
        RefreshToken token = refreshTokenServiceDb.saveRefreshToken(createRefreshToken("e6eeb33e-3688-11eb-adc1-0242ac120002"));
        //WHEN
        refreshTokenServiceDb.deleteRefreshTokenByToken(token.getToken());
        //THEN
        assertEquals(2, refreshTokenServiceDb.getAllRefreshTokens().size());
    }
}
