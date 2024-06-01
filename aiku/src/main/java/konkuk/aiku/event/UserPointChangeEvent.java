package konkuk.aiku.event;

import konkuk.aiku.domain.PointChangeType;
import konkuk.aiku.domain.PointType;
import konkuk.aiku.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserPointChangeEvent {
    private final Long userId;
    private final int point;
    private final PointType pointType;
    private final PointChangeType pointChangeType;
    private final LocalDateTime eventTime;
}
