package konkuk.aiku.event;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.firebase.dto.UserArrivalMessage;
import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.BettingService;
import konkuk.aiku.service.ScheduleService;
import konkuk.aiku.service.TitleProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


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
    public void scheduleEndEvent(ScheduleEndEvent event) {
        bettingService.setAllBettings(event.getScheduleId());

        // TODO: 스케줄 완료 알림 메시지


        // 칭호 조건 확인
        titleProviderService.titleProvider(event.getUserId());

    }

}
