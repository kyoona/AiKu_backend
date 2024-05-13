package konkuk.aiku.event;

import konkuk.aiku.controller.dto.RealTimeLocationDto;
import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleUserArrivalEvent {
    private final Users user;
    private final Schedule schedule;
    private final LocalDateTime arriveTime;
}
