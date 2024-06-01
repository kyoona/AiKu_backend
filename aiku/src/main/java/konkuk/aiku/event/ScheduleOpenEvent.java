package konkuk.aiku.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScheduleOpenEvent {
    protected Long scheduleId;
}
