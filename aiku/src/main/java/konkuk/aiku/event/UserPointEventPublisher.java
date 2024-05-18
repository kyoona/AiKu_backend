package konkuk.aiku.event;

import konkuk.aiku.domain.PointChangeType;
import konkuk.aiku.domain.PointType;
import konkuk.aiku.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserPointEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void userPointChangeEvent(Users users, int point, PointType pointType, PointChangeType pointChangeType, LocalDateTime eventTime) {
        publisher.publishEvent(new UserPointChangeEvent(users, point, pointType, pointChangeType, eventTime));
    }
}
