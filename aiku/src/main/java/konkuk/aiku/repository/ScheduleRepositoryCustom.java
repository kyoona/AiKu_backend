package konkuk.aiku.repository;

import konkuk.aiku.domain.*;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryCustom {
    Optional<UserSchedule> findUserScheduleByUserIdAndScheduleId(Long userId, Long scheduleId);
    List<Users> findWaitUsersInSchedule(Long groupId, List<UserSchedule> acceptUsers);
    Optional<UserArrivalData> findUserArrivalDataByUserIdAndScheduleId(Long userId, Long scheduleId);
    List<Schedule> findScheduleByGroupId(Long GroupId, String startTime, String endTime, ScheduleStatus status);
    List<Schedule> findScheduleByUserId(Long userId, String startTime, String endTime, ScheduleStatus status);
}
