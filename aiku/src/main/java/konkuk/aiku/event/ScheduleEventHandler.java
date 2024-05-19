package konkuk.aiku.event;

import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleEventHandler {

    private final ScheduleService scheduleService;
    private final AlarmService alarmService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInSchedule(UserArriveInScheduleEvent event) {
        Long userId = event.getUserId();
        Long scheduleId = event.getScheduleId();
        LocalDateTime arrivalTime = event.getArrivalTime();

        boolean isUserFirstArrival = scheduleService.createUserArrivalData(userId, scheduleId, arrivalTime);
        if (isUserFirstArrival) {
            alarmService.sendUserArrival(userId, scheduleId, arrivalTime);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void RegisterScheduleAlarmEvent(ScheduleAlarmEvent event){
        Long scheduleId = event.getScheduleId();


        Long delay = alarmService.getScheduleAlarmTimeDelay(scheduleId);
        Executors.newScheduledThreadPool(1)
                .schedule(alarmService.sendStartScheduleRunnable(scheduleId), delay, TimeUnit.MINUTES);

        if(delay > 1440){ //스케줄 예정시간과 등록시간의 차이가 24시간 이상일 경우->24시간 전 알람 발생
            Executors.newScheduledThreadPool(1)
                    .schedule(alarmService.sendNextScheduleRunnable(scheduleId), delay - 1440, TimeUnit.MINUTES);
        }
    }
}
