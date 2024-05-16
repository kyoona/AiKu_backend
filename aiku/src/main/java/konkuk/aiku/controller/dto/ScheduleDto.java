package konkuk.aiku.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import konkuk.aiku.service.dto.LocationServiceDto;
import konkuk.aiku.service.dto.ScheduleServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ScheduleDto {
    Long scheduleId;
    @NotBlank
    @Size(max = 15)
    private String scheduleName;
    @Valid
    private LocationDto location;
    @NotNull
    private LocalDateTime scheduleTime;

    public static ScheduleDto toDto(ScheduleServiceDto serviceDto){
        ScheduleDto scheduleDTO = ScheduleDto.builder()
                .scheduleId(serviceDto.getScheduleId())
                .scheduleName(serviceDto.getScheduleName())
                .location(LocationDto.toDto(serviceDto.getLocation()))
                .scheduleTime(serviceDto.getScheduleTime())
                .build();
        return scheduleDTO;
    }

    public ScheduleServiceDto toServiceDto(){
        return ScheduleServiceDto.builder()
                .scheduleName(this.scheduleName)
                .location(this.location.toServiceDto())
                .scheduleTime(this.scheduleTime)
                .build();
    }
}
