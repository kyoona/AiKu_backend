package konkuk.aiku.dto;

import konkuk.aiku.domain.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ScheduleSimpleResponseDTO {
    private Long scheduleId;
    private String scheduleName;
    private Location location;
    private LocalDateTime scheduleTime;
    private int memberSize;
    private LocalDateTime createdAt;
}
