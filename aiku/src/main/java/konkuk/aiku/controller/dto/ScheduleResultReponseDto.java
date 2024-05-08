package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.ScheduleResultServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ScheduleResultReponseDto {
    private ScheduleDto schedule;
    private int dataSize;
    @Builder.Default private List<UserArrivalDataDto> data = new ArrayList<>();

    public static ScheduleResultReponseDto toDto(ScheduleResultServiceDto serviceDto){
        ScheduleResultReponseDto dto = ScheduleResultReponseDto.builder()
                .schedule(ScheduleDto.toDto(serviceDto.getSchedule()))
                .dataSize(serviceDto.getDataSize())
                .data(UserArrivalDataDto.toDtoList(serviceDto.getData()))
                .build();
        return dto;
    }
}
