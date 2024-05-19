package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.ScheduleStatus;
import konkuk.aiku.service.dto.ScheduleSimpleServiceDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class ScheduleSimpleResponseDto {
    private Long scheduleId;
    private String scheduleName;
    private LocationDto location;
    private LocalDateTime scheduleTime;
    private ScheduleStatus status;
    private int userCount;
    private boolean accept;

    public static List<ScheduleSimpleResponseDto> toDtos(List<ScheduleSimpleServiceDto> serviceDtos) {
        List<ScheduleSimpleResponseDto> dtos = new ArrayList<>();
        serviceDtos.stream().forEach((serviceDto) -> dtos.add(toDto(serviceDto)));
        return dtos;
    }

    public static ScheduleSimpleResponseDto toDto(ScheduleSimpleServiceDto serviceDto){
        return ScheduleSimpleResponseDto.builder()
                .scheduleId(serviceDto.getScheduleId())
                .scheduleName(serviceDto.getScheduleName())
                .location(LocationDto.toDto(serviceDto.getLocation()))
                .scheduleTime(serviceDto.getScheduleTime())
                .status(serviceDto.getStatus())
                .userCount(serviceDto.getUserCount())
                .accept(serviceDto.isAccept())
                .build();
    }

}
