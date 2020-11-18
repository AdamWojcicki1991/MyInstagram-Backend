package com.myinstagram.repository;

import com.myinstagram.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    List<Post> findAll();

    // @Query("SELECT P FROM Post P ORDER BY P.postDate DESC")
    @Query(
            value = "SELECT P FROM POSTS P ORDER BY P.POST_DATE DESC", nativeQuery = true
    )
    List<Post> findAllSorted();

    // @Query("SELECT P FROM User U JOIN Post P ON U.id = P.user.id WHERE U.login = :login ORDER BY P.postDate DESC")
    @Query(
            value = "SELECT P FROM USERS U JOIN POSTS P ON U.ID = P.USER_ID WHERE U.LOGIN = :login ORDER BY P.POST_DATE DESC",
            nativeQuery = true
    )
    List<Post> findAllByLoginSorted(@Param("login") final String login);

    @Override
    Optional<Post> findById(final Long id);

    @Override
    Post save(final Post post);

    @Override
    void deleteById(final Long id);
}
