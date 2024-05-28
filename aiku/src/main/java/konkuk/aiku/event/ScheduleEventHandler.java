package konkuk.aiku.event;

import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.ScheduleService;
import konkuk.aiku.service.scheduler.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduleEventHandler {

    private final ScheduleService scheduleService;
    private final AlarmService alarmService;
    private final SchedulerService schedulerService;

    /**
     * 유저가 목적지에 도착 시 발생
     *  유저 도착 정보 생성
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInSchedule(UserArriveInScheduleEvent event) {
        Long userId = event.getUserId();
        Long scheduleId = event.getScheduleId();
        LocalDateTime arrivalTime = event.getArrivalTime();

        scheduleService.createUserArrivalData(userId, scheduleId, arrivalTime);

        alarmService.sendUserArrival(userId, scheduleId, arrivalTime);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerScheduleAlarmEvent(ScheduleAlarmEvent event){
        Long scheduleId = event.getScheduleId();

        Long delay = alarmService.getScheduleAlarmTimeDelay(scheduleId);

        schedulerService.addCurrentScheduleAlarm(scheduleId, alarmService.sendStartScheduleRunnable(scheduleId), delay);

        if(delay > 1440){ //스케줄 예정시간과 등록시간의 차이가 24시간 이상일 경우->24시간 전 알람 발생
            schedulerService.addNextScheduleAlarm(scheduleId, alarmService.sendNextScheduleRunnable(scheduleId), delay - 1440);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void registerScheduleDeleteEvent(ScheduleDeleteEvent event){
        Long scheduleId = event.getScheduleId();
        schedulerService.deleteScheduleAlarm(scheduleId);
    }
}
