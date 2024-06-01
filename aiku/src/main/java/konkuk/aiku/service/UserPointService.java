package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.event.UserPointEventPublisher;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPointService {

    private final UserPointRepository userPointRepository;
    private final ScheduleRepository scheduleRepository;

    private final UserPointEventPublisher userPointEventPublisher;

    public Long addUserPoint(Users users, int point, PointChangeType pointChangeType, PointType pointType) {
        UserPoint userPoint = UserPoint.builder()
                .user(users)
                .point(point)
                .pointChangeType(pointChangeType)
                .pointType(pointType)
                .build();

        UserPoint save = userPointRepository.save(userPoint);

        return save.getId();
    }

    //이벤트 메서드
    public void rewardSchedulePoint(Long scheduleId){
        Schedule schedule = findScheduleWithUser(scheduleId);
        schedule.getUsers().stream()
                .map(UserSchedule::getUser)
                .forEach((user) -> addUserPoint(user, RewardPoint.SCHEDULE_ADD, PointChangeType.PLUS, PointType.REWARD));
    }

    //레파지토리 조회 메서드
    public Schedule findScheduleWithUser(Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleWithUser(scheduleId).orElse(null);
        if (schedule == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }
}
