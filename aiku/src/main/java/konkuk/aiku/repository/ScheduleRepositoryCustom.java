package konkuk.aiku.repository;

import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryCustom {
    Optional<UserSchedule> findByUserIdAndScheduleId(Long userId, Long scheduleId);
    List<Users> findWaitUsersInSchedule(Long groupId, List<UserSchedule> acceptUsers);
}
