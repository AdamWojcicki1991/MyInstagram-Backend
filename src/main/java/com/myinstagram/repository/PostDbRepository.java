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
public interface PostDbRepository extends JpaRepository<Post, Long> {
    @Override
    List<Post> findAll();

    // @Query("SELECT P FROM Post P ORDER BY P.postDate DESC")
    @Query(value = "SELECT * FROM POSTS ORDER BY POST_DATE DESC", nativeQuery = true)
    List<Post> findAllSortedDescending();

    //    @Query("SELECT P FROM Post P JOIN User U ON P.user.id = U.id WHERE U.login = :login ORDER BY P.postDate DESC")
    @Query(
            value = "SELECT * FROM POSTS P JOIN USERS U ON P.USER_ID = U.ID WHERE U.LOGIN = :login ORDER BY P.POST_DATE DESC",
            nativeQuery = true
    )
    List<Post> findAllByLoginSortedDescending(@Param("login") final String login);

    @Override
    Optional<Post> findById(final Long id);

    @Override
    Post save(final Post post);

    @Override
    void deleteById(final Long id);
}
