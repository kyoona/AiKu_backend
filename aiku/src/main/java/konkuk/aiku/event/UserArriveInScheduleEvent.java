package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserArriveInScheduleEvent {
    private final Long userId;
    private final Long scheduleId;
    private final LocalDateTime arrivalTime;
}
