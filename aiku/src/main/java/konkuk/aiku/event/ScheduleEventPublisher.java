package konkuk.aiku.event;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void userArriveInSchedule(Users user, Schedule schedule){
        publisher.publishEvent(new ScheduleUserArrivalEvent(user, schedule, LocalDateTime.now()));
    }
}
