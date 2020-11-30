package com.myinstagram.service;

import com.myinstagram.domain.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@RequiredArgsConstructor
@Transactional
@Service
public class RefreshTokenService {
    private final RefreshTokenServiceDb refreshTokenServiceDb;
    private final PasswordProcessorService passwordProcessorService;

    public RefreshToken generateRefreshToken() {
        return refreshTokenServiceDb.saveRefreshToken(RefreshToken.builder()
                                                              .token(passwordProcessorService.generateUuid())
                                                              .createdDate(Instant.now())
                                                              .build());
    }

    public void validateRefreshToken(final String token) {
        refreshTokenServiceDb.getRefreshToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public void deleteRefreshToken(final String token) {
        refreshTokenServiceDb.deleteRefreshTokenByToken(token);
    }
}
