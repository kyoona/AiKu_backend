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
public class ScheduleDetailServiceDTO {
    private Long id;
    private String scheduleName;
    private LocationServiceDTO location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    @Builder.Default private List<UserSimpleServiceDTO> acceptUsers = new ArrayList<>();
    @Builder.Default private List<UserSimpleServiceDTO> waitUsers = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ScheduleDetailServiceDTO toDto(Schedule schedule, List<Users> waitUsers){

        ScheduleDetailServiceDTO scheduleDetailServiceDTO = ScheduleDetailServiceDTO.builder()
                .id(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .location(LocationServiceDTO.toDto(schedule.getLocation()))
                .scheduleTime(schedule.getScheduleTime())
                .acceptUsers(UserSimpleServiceDTO.toDtosByUserSchedule(schedule.getUsers()))
                .waitUsers(UserSimpleServiceDTO.toDtosByUser(waitUsers))
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
        return scheduleDetailServiceDTO;
    }
}
