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

    public void scheduleAddEvent(Long scheduleId, LocalDateTime scheduleTime){
        publisher.publishEvent(new ScheduleAddEvent(scheduleId, scheduleTime));
    }

    public void scheduleDeleteEvent(Long scheduleId){
        publisher.publishEvent(new ScheduleDeleteEvent(scheduleId));
    }

    public void scheduleOpenEvent(Long scheduleId){
        publisher.publishEvent(new ScheduleOpenEvent(scheduleId));
    }

    public void scheduleCloseEvent(Long scheduleId){
        publisher.publishEvent(new ScheduleCloseEvent(scheduleId));
    }
}
