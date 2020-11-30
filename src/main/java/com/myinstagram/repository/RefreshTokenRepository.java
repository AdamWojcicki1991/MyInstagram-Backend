package com.myinstagram.repository;

import com.myinstagram.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Override
    List<RefreshToken> findAll();

    @Override
    Optional<RefreshToken> findById(final Long id);

    Optional<RefreshToken> findByToken(final String token);

    @Override
    RefreshToken save(final RefreshToken refreshToken);

    @Override
    void deleteById(final Long id);

    void deleteByToken(final String token);

    @Override
    void delete(final RefreshToken refreshToken);
}
