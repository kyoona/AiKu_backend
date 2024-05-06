package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleResponseDTO {
    private Long scheduleId;
    private String scheduleName;
    private Location location;
    private LocalDateTime scheduleTime;
    @Builder.Default private List<UserSimpleResponseDTO> acceptUsers = new ArrayList<>();
    @Builder.Default private List<UserSimpleResponseDTO> waitUsers = new ArrayList<>();
    private LocalDateTime createdAt;
}
