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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void scheduleAddAlarmEvent(ScheduleAddEvent event){
        Long scheduleId = event.getScheduleId();
        LocalDateTime scheduleTime = event.getScheduleTime();

        Long delay = schedulerService.getTimeDelay(scheduleTime);

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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerScheduleAutoCloseEvent(ScheduleAddEvent event){
        Long scheduleId = event.getScheduleId();
        LocalDateTime scheduleTime = event.getScheduleTime();

        Runnable runnable = scheduleService.publishScheduleCloseEventRunnable(scheduleId);

        Long delay = schedulerService.getTimeDelay(scheduleTime);

        schedulerService.scheduleAutoClose(scheduleId, runnable, delay + 30);
    }

    //TODO
    /**
     * 스케줄 삭제 시 발생
     *  예약된 알림을 다 삭제해야 됨
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void registerScheduleDeleteEvent(ScheduleDeleteEvent event){
        Long scheduleId = event.getScheduleId();
        schedulerService.deleteScheduleAlarm(scheduleId);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInSchedule(UserArriveInScheduleEvent event) {
        Long userId = event.getUserId();
        Long scheduleId = event.getScheduleId();
        LocalDateTime arrivalTime = event.getArrivalTime();

        scheduleService.createUserArrivalData(userId, scheduleId, arrivalTime);

        alarmService.sendUserArrival(userId, scheduleId, arrivalTime);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void checkScheduleClose(UserArriveInScheduleEvent event) {
        Long scheduleId = event.getScheduleId();

        boolean finish = scheduleService.checkAllUserArrive(scheduleId);

        if (finish){
            scheduleService.publishScheduleCloseEvent(scheduleId);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void closeSchedule(ScheduleCloseEvent event) {
        Long scheduleId = event.getScheduleId();

        boolean isAllArrive = scheduleService.checkAllUserArrive(event.getScheduleId());
        if(!isAllArrive){
            scheduleService.createAllUserArrivalData(scheduleId);
        }

        alarmService.sendScheduleMapClose(scheduleId);
    }
}
