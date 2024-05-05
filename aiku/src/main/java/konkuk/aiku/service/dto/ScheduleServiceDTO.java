package konkuk.aiku.service.dto;

import konkuk.aiku.domain.ScheduleStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleServiceDTO {
    private Long id;
    private String scheduleName;
    private LocationServiceDTO location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
