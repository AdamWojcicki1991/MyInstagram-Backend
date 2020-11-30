package com.myinstagram.repository;

import com.myinstagram.domain.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Override
    List<VerificationToken> findAll();

    @Override
    Optional<VerificationToken> findById(final Long id);

    Optional<VerificationToken> findByToken(final String token);

    @Override
    VerificationToken save(final VerificationToken verificationToken);

    @Override
    void deleteById(final Long id);

    @Override
    void delete(final VerificationToken verificationToken);
}
