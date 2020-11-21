package com.myinstagram.repository;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.util.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface RoleDbRepository extends JpaRepository<Role, Long> {
    @Override
    List<Role> findAll();

    @Override
    Optional<Role> findById(final Long id);

    Optional<Role> findByRoleType(final RoleType roleType);

    @Override
    Role save(final Role role);

    @Override
    void deleteById(final Long id);
}
