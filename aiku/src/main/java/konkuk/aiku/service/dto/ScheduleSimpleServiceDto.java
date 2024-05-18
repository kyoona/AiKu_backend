package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.ScheduleStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleSimpleServiceDto {
    private Long scheduleId;
    private String scheduleName;
    private LocationServiceDto location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private int memberSize;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ScheduleSimpleServiceDto toDto(Schedule schedule) {
        return ScheduleSimpleServiceDto.builder()
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(LocationServiceDto.toDto(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .status(schedule.getStatus())
                .memberSize(schedule.getUserCount())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
