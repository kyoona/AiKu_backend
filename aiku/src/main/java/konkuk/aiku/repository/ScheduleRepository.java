package konkuk.aiku.repository;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom{
    @Query("SELECT us FROM UserSchedule us JOIN FETCH us.user WHERE us.schedule.id = :scheduleId")
    List<UserSchedule> findUsersByScheduleId(@Param("scheduleId") Long scheduleId);
}
