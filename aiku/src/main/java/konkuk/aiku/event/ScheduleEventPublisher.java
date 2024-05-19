package konkuk.aiku.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void userArriveInScheduleEvent(Long userId, Long scheduleId, LocalDateTime arrivalTime){
        publisher.publishEvent(new UserArriveInScheduleEvent(userId, scheduleId, arrivalTime));
    }

    public void scheduleAlarmEvent(Long scheduleId){
        publisher.publishEvent(new ScheduleAlarmEvent(scheduleId));
    }
}
