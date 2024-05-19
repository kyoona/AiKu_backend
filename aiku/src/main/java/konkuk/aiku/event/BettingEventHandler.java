package konkuk.aiku.event;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.firebase.dto.UserArrivalMessage;
import konkuk.aiku.service.AlarmService;
import konkuk.aiku.service.BettingService;
import konkuk.aiku.service.ScheduleService;
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
    private final MessageSender messageSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInBetting(UserArriveInScheduleEvent event) {
        Users user = event.getUser();
        Schedule schedule = event.getSchedule();

        bettingService.userRacingArrival(schedule.getId(), user.getId());

        // TODO: 베팅 완료 알림 메시지

//        boolean isUserFirstArrival = scheduleService.arriveUser(user, schedule.getId(), event.getArriveTime());
//        if (isUserFirstArrival) {
//            List<String> receiverTokens = createReceiverTokens(user, schedule.getUsers());
//            Map<String, String> messageDataMap = UserArrivalMessage.createMessage(user, schedule, event.getArriveTime())
//                    .toStringMap();
//            messageSender.sendMessageToUsers(messageDataMap, receiverTokens);
//        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void scheduleEndEvent(ScheduleEndEvent event) {
        Schedule schedule = event.getSchedule();

        bettingService.setAllBettings(schedule.getId());

        // TODO: 스케줄 완료 알림 메시지

//        boolean isUserFirstArrival = scheduleService.arriveUser(user, schedule.getId(), event.getArriveTime());
//        if (isUserFirstArrival) {
//            List<String> receiverTokens = createReceiverTokens(user, schedule.getUsers());
//            Map<String, String> messageDataMap = UserArrivalMessage.createMessage(user, schedule, event.getArriveTime())
//                    .toStringMap();
//            messageSender.sendMessageToUsers(messageDataMap, receiverTokens);
//        }
    }

}
