package com.myinstagram.service;

import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.exceptions.custom.security.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RefreshTokenService {
    private final RefreshTokenServiceDb refreshTokenServiceDb;
    private final PasswordProcessorService passwordProcessorService;

    public List<RefreshToken> getRefreshTokens() {
        return refreshTokenServiceDb.getAllRefreshTokens();
    }

    public RefreshToken generateRefreshToken() {
        return refreshTokenServiceDb.saveRefreshToken(RefreshToken.builder()
                                                              .token(passwordProcessorService.generateUuid())
                                                              .createdDate(Instant.now())
                                                              .build());
    }

    public RefreshToken validateRefreshToken(final String token) {
        return refreshTokenServiceDb.getRefreshToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException(token));
    }

    public void deleteRefreshToken(final String token) {
        refreshTokenServiceDb.deleteRefreshTokenByToken(token);
    }
}
