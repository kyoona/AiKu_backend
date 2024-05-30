package konkuk.aiku.event;

import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.BettingService;
import konkuk.aiku.service.TitleProviderService;
import konkuk.aiku.service.scheduler.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
@Slf4j
public class BettingEventHandler {

    private final BettingService bettingService;
    private final TitleProviderService titleProviderService;
    private final AlarmService alarmService;
    private final SchedulerService schedulerService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInBetting(UserArriveInScheduleEvent event) {
        // 유저 도착시 베팅 종료 로직
        bettingService.userRacingArrival(event.getScheduleId(), event.getUserId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void racingApplyEvent(RacingApplyEvent event) {
        // 베팅 시작 메시지
        alarmService.sendBettingNew(event.getBettingId());

        // 베팅 수락을 기다리는 로직 (10초 후 종료)
        Runnable deleteBettingRunnable = bettingService.deleteBettingById(event.getBettingId());
        schedulerService.bettingAcceptDelay(event.getBettingId(), deleteBettingRunnable);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void racingAcceptEvent(RacingAcceptEvent event) {
        // 레이싱이 성사되었다는 메시지 To Bettor
        alarmService.sendBettingAcceptOrDeny(event.getBettingId(), true);
        // 레이싱이 성사되었다는 메시지 To Everyone
        alarmService.sendBettingStart(event.getBettingId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void racingDenyEvent(RacingDenyEvent event) {
        // 레이싱이 거절되었다는 메시지 To Bettor
        alarmService.sendBettingAcceptOrDeny(event.getBettingId(), false);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void racingEndEvent(RacingEndEvent event) {
        // 베팅 완료 알림 메시지
        alarmService.sendBettingFinish(event.getBettingId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void scheduleEndEvent(ScheduleCloseEvent event) {
        Long scheduleId = event.getScheduleId();

        // 끝난 스케줄의 베팅 정리
        bettingService.setAllBettings(scheduleId);

        // 칭호 부여 로직 검증
        titleProviderService.scheduleEndTitleProvider(scheduleId);
    }

}
