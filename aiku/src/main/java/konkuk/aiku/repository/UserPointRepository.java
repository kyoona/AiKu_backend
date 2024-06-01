package konkuk.aiku.repository;

import konkuk.aiku.domain.UserPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface UserPointRepository extends JpaRepository<UserPoint, Long> {
    @Query("select u from UserPoint u where u.user.id = ?1 and u.createdAt between ?2 and ?3")
    List<UserPoint> findAllByUserIdAndCreatedAtBetween(Long id, LocalDateTime scheduleStartTime, LocalDateTime scheduleEndTime);
}
