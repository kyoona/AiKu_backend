package konkuk.aiku.event;

import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.service.BettingService;
import konkuk.aiku.service.ScheduleService;
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
    private final MessageSender messageSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInBetting(UserArriveInScheduleEvent event) {

        // TODO: 베팅 완료 알림 메시지

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void scheduleEndEvent(ScheduleCloseEvent event) {
        Long scheduleId = event.getScheduleId();

        bettingService.setAllBettings(scheduleId);

        // TODO: 스케줄 완료 알림 메시지


        // 칭호 조건 확인
//        titleProviderService.titleProvider(event.getUserId());

    }

}
