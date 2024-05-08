package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.ScheduleDetailServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleResponseDto {
    private Long scheduleId;
    private String scheduleName;
    private LocationDto location;
    private LocalDateTime scheduleTime;
    @Builder.Default private List<UserSimpleResponseDto> acceptUsers = new ArrayList<>();
    @Builder.Default private List<UserSimpleResponseDto> waitUsers = new ArrayList<>();
    private LocalDateTime createdAt;

    public static ScheduleResponseDto toDto(ScheduleDetailServiceDto serviceDto){
        ScheduleResponseDto dto = ScheduleResponseDto.builder()
                .scheduleId(serviceDto.getId())
                .scheduleName(serviceDto.getScheduleName())
                .location(LocationDto.toDto(serviceDto.getLocation()))
                .scheduleTime(serviceDto.getScheduleTime())
                .acceptUsers(UserSimpleResponseDto.toDtos(serviceDto.getAcceptUsers()))
                .waitUsers(UserSimpleResponseDto.toDtos(serviceDto.getWaitUsers()))
                .createdAt(serviceDto.getCreatedAt())
                .build();
        return dto;
    }
}
