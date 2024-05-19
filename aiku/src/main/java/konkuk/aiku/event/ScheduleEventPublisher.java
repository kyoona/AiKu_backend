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

    public void userArriveInScheduleEvent(Users user, Schedule schedule, LocalDateTime arrivalTime){
        publisher.publishEvent(new UserArriveInScheduleEvent(user, schedule, arrivalTime));
    }

    public void scheduleAlarmEvent(Long scheduleId){
        publisher.publishEvent(new ScheduleAlarmEvent(scheduleId));
    }
}
