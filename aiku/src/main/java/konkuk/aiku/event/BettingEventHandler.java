package konkuk.aiku.event;

import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.BettingService;
import konkuk.aiku.service.TitleProviderService;
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
        alarmService.sendBettingStart(event.getTargetId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void racingAcceptEvent(RacingAcceptEvent event) {
        // 베팅이 신청되었다는 메시지
        alarmService.sendBettingStart(event.getBettorId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void racingEndEvent(RacingEndEvent event) {
        // 베팅 완료 알림 메시지
        alarmService.sendBettingFinish(event.getBettorId());
        alarmService.sendBettingFinish(event.getTargetId());
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
