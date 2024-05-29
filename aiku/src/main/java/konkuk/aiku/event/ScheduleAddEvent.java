package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ScheduleAddEvent {
    private final Long scheduleId;
    private final LocalDateTime scheduleTime;
}
