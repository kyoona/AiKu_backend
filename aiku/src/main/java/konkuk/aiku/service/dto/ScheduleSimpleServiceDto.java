package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.ScheduleStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleSimpleServiceDto {
    private static final Logger log = LoggerFactory.getLogger(ScheduleSimpleServiceDto.class);
    private Long scheduleId;
    private String scheduleName;
    private LocationServiceDto location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private int userCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean accept;

    public static ScheduleSimpleServiceDto toDto(Schedule schedule) {
        ScheduleSimpleServiceDto dto = ScheduleSimpleServiceDto.builder()
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(LocationServiceDto.toDto(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .status(schedule.getStatus())
                .userCount(schedule.getUserCount())
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .accept(true)
                .build();
        dto.setAccept(true);
        return dto;
    }

    public ScheduleSimpleServiceDto setAccept(boolean accept){
        this.accept = accept;
        return this;
    }
}
