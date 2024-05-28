package konkuk.aiku.repository;

import konkuk.aiku.domain.*;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepositoryCustom {
    Optional<Schedule> findScheduleWithUser(Long scheduleId);
    List<Schedule> findScheduleWithUserByGroupId(Long GroupId, String startTime, String endTime, ScheduleStatus status);

    Optional<UserSchedule> findUserScheduleByUserIdAndScheduleId(Long userId, Long scheduleId);
    List<UserSchedule> findUserScheduleByUserId(Long userId, String startTime, String endTime, ScheduleStatus status);

    Optional<UserArrivalData> findUserArrivalDataByUserIdAndScheduleId(Long userId, Long scheduleId);
    List<UserArrivalData> findUserArrivalDatasWithUserByGroupId(Long groupId);
}
