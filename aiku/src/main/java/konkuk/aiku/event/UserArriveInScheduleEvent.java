package konkuk.aiku.event;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserArriveInScheduleEvent {
    private final Users user;
    private final Schedule schedule;
    private final LocalDateTime arriveTime;
}
