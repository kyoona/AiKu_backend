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

    public void scheduleEndBettingEvent(Schedule schedule){
        publisher.publishEvent(new ScheduleCloseEvent(schedule.getId()));
    }

}
