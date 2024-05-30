package konkuk.aiku.event;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BettingEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void userArriveInBettingEvent(Users user, Schedule schedule){
        publisher.publishEvent(new UserArriveInBettingEvent(user, schedule));
    }

    public void racingApplyEvent(Long scheduleId, Long bettingId, Long targetId){
        publisher.publishEvent(new RacingApplyEvent(scheduleId, bettingId, targetId));
    }

    public void racingAcceptEvent(Long scheduleId, Long bettingId, Long bettorId){
        publisher.publishEvent(new RacingAcceptEvent(scheduleId, bettingId, bettorId));
    }

    public void racingEndEvent(Long scheduleId, Long bettingId, Long bettorId, Long targetId){
        publisher.publishEvent(new RacingEndEvent(scheduleId, bettingId, bettorId, targetId));
    }

    public void scheduleEndBettingEvent(Schedule schedule){
        publisher.publishEvent(new ScheduleCloseEvent(schedule.getId()));
    }

}
