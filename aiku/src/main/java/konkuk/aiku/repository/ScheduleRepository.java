package konkuk.aiku.repository;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserArrivalData;
import konkuk.aiku.domain.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom{
    @Query("SELECT us FROM UserSchedule us WHERE us.schedule.id = :scheduleId AND us.user.id = :userId")
    Optional<UserSchedule> findUserScheduleByUserIdAndScheduleId(@Param("userId") Long userId,@Param("scheduleId") Long scheduleId);
    @Query("SELECT s FROM Schedule s WHERE s.group.id = :groupId AND s.id = :scheduleId")
    Optional<Schedule> findScheduleByGroupIdAndScheduleId(@Param("groupId") Long groupId, @Param("scheduleId") Long scheduleId);

    @Query("SELECT COUNT(us) FROM UserSchedule us WHERE us.schedule.id = :scheduleId")
    int countOfScheduleUser(@Param("scheduleId") Long scheduleId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Schedule s SET s.userCount = s.userCount + 1 WHERE s.id = :scheduleId")
    void upScheduleUserCount(@Param("scheduleId") Long scheduleId);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Schedule s SET s.userCount = s.userCount - 1 WHERE s.id = :scheduleId")
    void downScheduleUserCount(@Param("scheduleId") Long scheduleId);

    @Query("SELECT uad FROM UserArrivalData uad WHERE uad.user.id = :userId")
    List<UserArrivalData> findUserArrivalDataByUserId(Long userId);
}
