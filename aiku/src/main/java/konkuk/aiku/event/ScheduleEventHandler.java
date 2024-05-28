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
     * 스케줄 등록 시 발생
     *  스케줄 24시간 전 푸시 알림 예약
     *  스케줄 당일 푸시 알림 예약
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerScheduleAlarmEvent(ScheduleAlarmEvent event){
        Long scheduleId = event.getScheduleId();

        Long delay = alarmService.getScheduleAlarmTimeDelay(scheduleId);

        //스케줄 이전
        if(delay > 1440){//24시
            schedulerService.addNextScheduleAlarm(scheduleId, alarmService.sendNextScheduleRunnable(scheduleId), delay - 1440);
        }
        if (delay > 30){
            schedulerService.addScheduleMapOpenAlarm(scheduleId, alarmService.sendScheduleMapOpenRunnable(scheduleId), delay - 30);
        }

        //스케줄 시간 완료
        schedulerService.addScheduleFinishAlarm(scheduleId, alarmService.sendScheduleFinishRunnable(scheduleId), delay);
    }

    /**
     * 스케줄 삭제 시 발생
     *  예약된 알림을 다 삭제해야 됨
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void registerScheduleDeleteEvent(ScheduleDeleteEvent event){
        Long scheduleId = event.getScheduleId();
        schedulerService.deleteScheduleAlarm(scheduleId);
    }

    /**
     * 유저가 목적지에 도착 시 발생
     *  유저 도착 정보 생성
     *  유저 도착 푸시 알림
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
}
