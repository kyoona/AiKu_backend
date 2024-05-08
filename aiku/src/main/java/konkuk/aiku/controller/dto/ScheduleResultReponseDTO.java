package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.ScheduleResultServiceDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ScheduleResultReponseDTO {
    private ScheduleDTO schedule;
    private int dataSize;
    @Builder.Default private List<UserArrivalDataDTO> data = new ArrayList<>();

    public static ScheduleResultReponseDTO toDto(ScheduleResultServiceDTO serviceDto){
        ScheduleResultReponseDTO dto = ScheduleResultReponseDTO.builder()
                .schedule(ScheduleDTO.toDto(serviceDto.getSchedule()))
                .dataSize(serviceDto.getDataSize())
                .data(UserArrivalDataDTO.toDtoList(serviceDto.getData()))
                .build();
        return dto;
    }
}
