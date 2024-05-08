package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.ScheduleStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleServiceDTO {
    private Long scheduleId;
    private String scheduleName;
    private LocationServiceDTO location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ScheduleServiceDTO toDto(Schedule schedule){
        ScheduleServiceDTO dto = ScheduleServiceDTO.builder()
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(LocationServiceDTO.toDto(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .status(schedule.getStatus())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
        return dto;
    }
}
