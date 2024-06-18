package december.spring.studywithme.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import december.spring.studywithme.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    @Query("select post from Post post " +
            "where post.createdAt >= :startDate and post.createdAt < :finishDate")
    Page<Post> findPostPageByPeriod(@Param("startDate") LocalDateTime startDate, @Param("finishDate") LocalDateTime finishDate, Pageable pageable);

    @Query("select post from Post post " +
            "where post.createdAt >= :startDate")
    Page<Post> findPostPageByStartDate(@Param("startDate") LocalDateTime startDate, Pageable pageable);

    @Query("select post from Post post " +
            "where post.createdAt < :finishDate")
    Page<Post> findPostPageByFinishDate(@Param("finishDate")LocalDateTime finishDate, Pageable pageable);
}
