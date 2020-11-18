package com.myinstagram.repository;

import com.myinstagram.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    List<Role> findAll();

    @Override
    Optional<Role> findById(final Long id);

    Optional<Role> findByRoleName(final String roleName);

    @Override
    Role save(final Role role);

    @Override
    void deleteById(final Long id);
}
