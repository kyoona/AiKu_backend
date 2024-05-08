package konkuk.aiku.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleDTO {
    Long scheduleId;
    @NotBlank
    @Size(max = 15)
    private String scheduleName;
    @Valid
    private LocationDTO location;
    @NotNull
    private LocalDateTime scheduleTime;

    public static ScheduleDTO toDto(ScheduleServiceDTO serviceDto){
        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .scheduleId(serviceDto.getScheduleId())
                .scheduleName(serviceDto.getScheduleName())
                .location(LocationDTO.toDto(serviceDto.getLocation()))
                .scheduleTime(serviceDto.getScheduleTime())
                .build();
        return scheduleDTO;
    }
}
