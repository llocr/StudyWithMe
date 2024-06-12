package december.spring.studywithme.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import december.spring.studywithme.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByPostIdAndId(Long postId, Long commentId);
}
