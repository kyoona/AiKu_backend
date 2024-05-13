package konkuk.aiku.repository;

import konkuk.aiku.domain.UserArrivalData;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryCustom {
    Optional<UserSchedule> findUserScheduleByUserIdAndScheduleId(Long userId, Long scheduleId);
    List<Users> findWaitUsersInSchedule(Long groupId, List<UserSchedule> acceptUsers);
    Optional<UserArrivalData> findUserArrivalDataByUserIdAndScheduleId(Long userId, Long scheduleId);
}
