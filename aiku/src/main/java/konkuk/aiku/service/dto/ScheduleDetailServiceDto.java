package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.ScheduleStatus;
import konkuk.aiku.domain.Users;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleDetailServiceDto {
    private Long id;
    private String scheduleName;
    private LocationServiceDto location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private int userCount;
    @Builder.Default private List<UserSimpleServiceDto> acceptUsers = new ArrayList<>();
    @Builder.Default private List<UserSimpleServiceDto> waitUsers = new ArrayList<>();
    private Long targetUserId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ScheduleDetailServiceDto toDto(Schedule schedule, List<Users> waitUsers, Long targetUserId){

        ScheduleDetailServiceDto scheduleDetailServiceDTO = ScheduleDetailServiceDto.builder()
                .id(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(LocationServiceDto.toDto(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .userCount(schedule.getUserCount())
                .acceptUsers(UserSimpleServiceDto.toDtosByUserSchedule(schedule.getUsers()))
                .waitUsers(UserSimpleServiceDto.toDtosByUser(waitUsers))
                .targetUserId(targetUserId)
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
        return scheduleDetailServiceDTO;
    }
}
