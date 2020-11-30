package com.myinstagram.service;

import com.myinstagram.domain.entity.RefreshToken;
import com.myinstagram.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceDb {
    private final RefreshTokenRepository refreshTokenRepository;

    public List<RefreshToken> getAllRefreshTokens() {
        return refreshTokenRepository.findAll();
    }

    public Optional<RefreshToken> getRefreshToken(final String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken saveRefreshToken(final RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshTokenById(final Long id) {
        refreshTokenRepository.deleteById(id);
    }

    public void deleteRefreshTokenByToken(final String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
