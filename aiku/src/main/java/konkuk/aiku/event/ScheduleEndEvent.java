package konkuk.aiku.event;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleEndEvent {
    private Schedule schedule;
}
