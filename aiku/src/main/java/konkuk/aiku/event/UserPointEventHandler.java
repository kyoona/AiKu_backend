package konkuk.aiku.event;

import konkuk.aiku.domain.Users;
import konkuk.aiku.service.UserPointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserPointEventHandler {
    private final UserPointService userPointService;


    // Point 이동 이벤트 발생
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void userPointChangeEvent(UserPointChangeEvent event) {
        Users user = event.getUser();

        userPointService.addUserPoint(user, event.getPoint(), event.getPointChangeType(), event.getPointType());
    }
}
