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

    //스케줄 등록 -> 약속 전날, 약속 시간 30분 전(맵 오픈), 약속 시간 푸시 알림
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

        log.info("딜레이 {}", delay);
        //스케줄 시간 완료
        schedulerService.addScheduleFinishAlarm(scheduleId, alarmService.sendScheduleFinishRunnable(scheduleId), delay);

        log.info("Handel ScheduleAddEvent completion");
    }

    //스케줄 등록 -> 약속 시간 30분 후 스케줄 자동 종료 이벤트 발생
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void registerScheduleAutoCloseEvent(ScheduleAddEvent event){
        Long scheduleId = event.getScheduleId();
        LocalDateTime scheduleTime = event.getScheduleTime();

        Runnable runnable = scheduleService.publishScheduleCloseEventRunnable(scheduleId);

        Long delay = schedulerService.getTimeDelay(scheduleTime);

        schedulerService.scheduleAutoClose(scheduleId, runnable, delay + 30);
        log.info("Handel ScheduleAddEvent completion");
    }

    //스케줄 종료 이벤트 -> 모든 유저가 도착했는지 검증(맵 자동 종료 or 유저 전원 도착), 검증 후 유저 도착 정보 생성, 맵 닫힘 알림
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void closeSchedule(ScheduleCloseEvent event) {
        Long scheduleId = event.getScheduleId();

        boolean isAllArrive = scheduleService.checkAllUserArrive(event.getScheduleId());
        if(!isAllArrive){
            scheduleService.createAllUserArrivalData(scheduleId);
        }

        alarmService.sendScheduleMapClose(scheduleId);
        log.info("Handel ScheduleCloseEvent completion");
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

    //유저 도착 -> 도착 정보 생성, 도착 알림
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInSchedule(UserArriveInScheduleEvent event) {
        Long userId = event.getUserId();
        Long scheduleId = event.getScheduleId();
        LocalDateTime arrivalTime = event.getArrivalTime();

        scheduleService.createUserArrivalData(userId, scheduleId, arrivalTime);

        alarmService.sendUserArrival(userId, scheduleId, arrivalTime);
        log.info("Handel UserArriveInScheduleEvent completion");
    }

    //유저 도착 -> 모든 유저가 도착했는지 검증, 검증 후 스케줄 종료 이벤드 발생
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void checkScheduleClose(UserArriveInScheduleEvent event) {
        Long scheduleId = event.getScheduleId();

        boolean finish = scheduleService.checkAllUserArrive(scheduleId);

        if (finish){
            scheduleService.publishScheduleCloseEvent(scheduleId);
        }
        log.info("Handel UserArriveInScheduleEvent completion");
    }
}
