package konkuk.aiku.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ScheduleServiceDTO {
    private Long id;
    private String scheduleName;
    private LocationServiceDTO location;
    private LocalDateTime scheduleTime;
}
