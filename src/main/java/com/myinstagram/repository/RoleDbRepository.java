package com.myinstagram.repository;

import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Role> findAllByRoleType(final RoleType roleType);

    @Query(
            value = "SELECT * FROM ROLES R INNER JOIN USER_ROLES UR ON R.ID = UR.ROLE_ID INNER JOIN USERS U ON UR.USER_ID = U.ID WHERE U.LOGIN = :login",
            nativeQuery = true
    )
    List<Role> findAllByLogin(@Param("login") final String login);

    @Override
    Role save(final Role role);

    @Override
    void deleteById(final Long id);

    @Override
    void delete(final Role role);
}
