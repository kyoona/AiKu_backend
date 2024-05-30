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

    public void racingApplyEvent(Long bettingId, Long targetId){
        publisher.publishEvent(new RacingApplyEvent(bettingId, targetId));
    }

    public void racingAcceptEvent(Long bettorId){
        publisher.publishEvent(new RacingAcceptEvent(bettorId));
    }

    public void racingEndEvent(Long bettorId, Long targetId){
        publisher.publishEvent(new RacingEndEvent(bettorId, targetId));
    }

    public void scheduleEndBettingEvent(Schedule schedule){
        publisher.publishEvent(new ScheduleCloseEvent(schedule.getId()));
    }

}
