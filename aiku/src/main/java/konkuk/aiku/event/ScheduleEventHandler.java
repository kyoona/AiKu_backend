package konkuk.aiku.event;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.firebase.dto.UserArrivalMessage;
import konkuk.aiku.service.AlarmService;
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
public class ScheduleEventHandler {

    private final ScheduleService scheduleService;
    private final AlarmService alarmService;
    private final MessageSender messageSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userArriveInSchedule(UserArriveInScheduleEvent event) {
        Users user = event.getUser();
        Schedule schedule = event.getSchedule();

        boolean isUserFirstArrival = scheduleService.arriveUser(user, schedule.getId(), event.getArriveTime());
        if (isUserFirstArrival) {
            List<String> receiverTokens = createReceiverTokens(user, schedule.getUsers());
            Map<String, String> messageDataMap = UserArrivalMessage.createMessage(user, schedule, event.getArriveTime())
                    .toStringMap();
            messageSender.sendMessageToUsers(messageDataMap, receiverTokens);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void RegisterScheduleAlarmEvent(ScheduleAlarmEvent event){
        Long scheduleId = event.getScheduleId();


        Long delay = 0l;
        Executors.newScheduledThreadPool(1)
                        .schedule(() -> {}, delay, TimeUnit.DAYS);
    }

    //==편의 메서드==
    private List<String> createReceiverTokens(Users user, List<UserSchedule> userSchedules){
        List<String> receiverToken = new ArrayList<>();
        for (UserSchedule userSchedule : userSchedules) {
            Users scheUser = userSchedule.getUser();
            if (scheUser.getId() != user.getId()) {
                receiverToken.add(scheUser.getFcmToken());
            }
        }
        return receiverToken;
    }
}
