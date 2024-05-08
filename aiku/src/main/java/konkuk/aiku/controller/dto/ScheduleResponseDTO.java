package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Location;
import konkuk.aiku.service.dto.ScheduleDetailServiceDTO;
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
    private LocationDTO location;
    private LocalDateTime scheduleTime;
    @Builder.Default private List<UserSimpleResponseDTO> acceptUsers = new ArrayList<>();
    @Builder.Default private List<UserSimpleResponseDTO> waitUsers = new ArrayList<>();
    private LocalDateTime createdAt;

    public static ScheduleResponseDTO toDto(ScheduleDetailServiceDTO serviceDto){
        ScheduleResponseDTO dto = ScheduleResponseDTO.builder()
                .scheduleId(serviceDto.getId())
                .scheduleName(serviceDto.getScheduleName())
                .location(LocationDTO.toDto(serviceDto.getLocation()))
                .scheduleTime(serviceDto.getScheduleTime())
                .acceptUsers(UserSimpleResponseDTO.toDtos(serviceDto.getAcceptUsers()))
                .waitUsers(UserSimpleResponseDTO.toDtos(serviceDto.getWaitUsers()))
                .createdAt(serviceDto.getCreatedAt())
                .build();
        return dto;
    }
}
