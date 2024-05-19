package konkuk.aiku.repository;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom{
    @Query("SELECT us FROM UserSchedule us JOIN FETCH us.user WHERE us.schedule.id = :scheduleId")
    List<UserSchedule> findUsersByScheduleId(@Param("scheduleId") Long scheduleId);
    @Query("SELECT us FROM UserSchedule us WHERE us.schedule.id = :scheduleId AND us.user.id = :userId")
    Optional<UserSchedule> findUserScheduleByUserIdAndScheduleId(@Param("userId") Long userId,@Param("scheduleId") Long scheduleId);
    @Query("SELECT s.scheduleTime FROM Schedule s WHERE s.group.id = :groupId ORDER BY s.scheduleTime DESC")
    Optional<LocalDateTime> findLatestScheduleTimeByGroupId(@Param("groupId") Long groupId);
    @Modifying
    @Query("UPDATE Schedule s SET s.userCount = s.userCount + 1 WHERE s.id = :scheduleId")
    void upScheduleUserCount(@Param("scheduleId") Long scheduleId);
}
