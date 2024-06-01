package konkuk.aiku.event;

import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.ScheduleService;
import konkuk.aiku.service.UserPointService;
import konkuk.aiku.service.scheduler.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
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
    private final UserPointService userPointService;

    //스케줄 등록 -> 약속 전날, 약속 시간 30분 전(맵 오픈), 약속 시간 푸시 알림
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void scheduleAddAlarmEvent(ScheduleAddEvent event){
        Long scheduleId = event.getScheduleId();
        LocalDateTime scheduleTime = event.getScheduleTime();

        Long delay = schedulerService.getTimeDelay(scheduleTime);

        //스케줄 24시전
        if(delay > 1440){
            schedulerService.addNextScheduleAlarm(scheduleId, alarmService.sendNextScheduleRunnable(scheduleId), delay - 1440);
        }

        //스케줄 30분 전
/*        if (delay > 30) {
            schedulerService.addScheduleMapOpenAlarm(scheduleId, scheduleService.publishScheduleMapOpenRunnable(scheduleId), delay - 30);
        } else {
            scheduleService.publishScheduleMapOpen(scheduleId);
        }*/

        //TODO test용
        if (delay > 3) {
            schedulerService.addScheduleMapOpenAlarm(scheduleId, scheduleService.publishScheduleMapOpenRunnable(scheduleId), delay - 3);
        } else {
            scheduleService.publishScheduleMapOpen(scheduleId);
        }

        //스케줄 시간
        schedulerService.addScheduleFinishAlarm(scheduleId, alarmService.sendScheduleFinishRunnable(scheduleId), delay);

        log.info("Handel ScheduleAddEvent completion");
    }

    //맵 오픈(스케줄 30분 전) -> 스케줄 상태 변경, 아쿠 지급, 맵 오픈 알림
    @Async
    @EventListener
    public void openSchedule(ScheduleOpenEvent event){
        Long scheduleId = event.getScheduleId();

        scheduleService.openScheduleMap(scheduleId);

        userPointService.rewardSchedulePoint(scheduleId);

        alarmService.sendScheduleMapOpen(scheduleId);
        log.info("Handel ScheduleOpenEvent completion");
    }

    //스케줄 등록 -> 약속 시간 30분 후 스케줄 자동 종료 이벤트 발생
    @Async
    @EventListener
    public void registerScheduleAutoCloseEvent(ScheduleAddEvent event){
        Long scheduleId = event.getScheduleId();
        LocalDateTime scheduleTime = event.getScheduleTime();

        Runnable runnable = scheduleService.publishScheduleCloseEventRunnable(scheduleId);

        Long delay = schedulerService.getTimeDelay(scheduleTime);

//        schedulerService.scheduleAutoClose(scheduleId, runnable, delay + 30);

        //TODO test용
        schedulerService.scheduleAutoClose(scheduleId, runnable, delay + 3);
        log.info("Handel ScheduleAddEvent completion");
    }

    //스케줄 종료 이벤트 -> 모든 유저가 도착했는지 검증(맵 자동 종료 or 유저 전원 도착), 검증 후 유저 도착 정보 생성, 맵 닫힘 알림
    @Async
    @EventListener
    public void closeSchedule(ScheduleCloseEvent event) {
        Long scheduleId = event.getScheduleId();

        boolean isAlreadyTerm = scheduleService.closeScheduleMap(scheduleId);
        if (isAlreadyTerm) {
            return; //종료된 스케줄
        }

        boolean isAllArrive = scheduleService.checkAllUserArrive(scheduleId);
        if(!isAllArrive){
            scheduleService.createAllUserArrivalData(scheduleId);
        }

        alarmService.sendScheduleMapClose(scheduleId);
        log.info("Handel ScheduleCloseEvent completion");
    }

    //스케줄 삭제 -> 스케줄과 관련된 예약된 작업들 삭제
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void registerScheduleDeleteEvent(ScheduleDeleteEvent event){
        Long scheduleId = event.getScheduleId();
        schedulerService.deleteSchedule(scheduleId);
        log.info("Handel ScheduleDeleteEvent completion");
    }

    //유저 도착 -> 도착 알림
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInSchedule(UserArriveInScheduleEvent event) {
        Long userId = event.getUserId();
        Long scheduleId = event.getScheduleId();
        LocalDateTime arrivalTime = event.getArrivalTime();

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
