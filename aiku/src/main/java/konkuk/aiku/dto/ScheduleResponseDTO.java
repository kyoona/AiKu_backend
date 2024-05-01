package konkuk.aiku.dto;

import konkuk.aiku.domain.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ScheduleResponseDTO {
    private Long scheduleId;
    private String scheduleName;
    private Location location;
    private LocalDateTime scheduleTime;
    private List<UserSimpleResponseDTO> users;
    private LocalDateTime createdAt;
}
