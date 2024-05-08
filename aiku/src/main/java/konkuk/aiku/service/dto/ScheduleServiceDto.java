package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.ScheduleStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleServiceDto {
    private Long scheduleId;
    private String scheduleName;
    private LocationServiceDto location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ScheduleServiceDto toDto(Schedule schedule){
        ScheduleServiceDto dto = ScheduleServiceDto.builder()
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(LocationServiceDto.toDto(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .status(schedule.getStatus())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
        return dto;
    }
}
