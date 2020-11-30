package com.myinstagram.service;

import com.myinstagram.domain.entity.VerificationToken;
import com.myinstagram.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VerificationTokenServiceDb {
    private final VerificationTokenRepository verificationTokenRepository;

    public List<VerificationToken> getAllVerificationTokens() {
        return verificationTokenRepository.findAll();
    }

    public Optional<VerificationToken> getVerificationTokenByToken(final String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public VerificationToken saveVerificationToken(final VerificationToken verificationToken) {
        return verificationTokenRepository.save(verificationToken);
    }

    public void deleteVerificationToken(final VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
