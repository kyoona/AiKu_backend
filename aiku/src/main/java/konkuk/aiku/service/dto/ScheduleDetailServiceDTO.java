package konkuk.aiku.service.dto;

import konkuk.aiku.domain.ScheduleStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleDetailServiceDTO {
    private Long id;
    private String scheduleName;
    private LocationServiceDTO location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    @Builder.Default private List<UserSimpleServiceDTO> users = new ArrayList<>();
    @Builder.Default private List<UserSimpleServiceDTO> waitUsers = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
