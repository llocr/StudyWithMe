package december.spring.studywithme.repository;

import december.spring.studywithme.entity.ApiUseTime;
import december.spring.studywithme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUseTimeRepository extends JpaRepository<ApiUseTime, Long> {
    Optional<ApiUseTime> findByUser(User user);
}