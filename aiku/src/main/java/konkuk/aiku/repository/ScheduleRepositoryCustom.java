package konkuk.aiku.repository;

import konkuk.aiku.domain.UserSchedule;

import java.util.Optional;

public interface ScheduleRepositoryCustom {
    Optional<UserSchedule> findByUserIdAndScheduleId(Long userId, Long scheduleId);
}
