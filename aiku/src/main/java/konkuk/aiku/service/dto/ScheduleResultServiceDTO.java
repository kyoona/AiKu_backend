package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserArrivalData;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleResultServiceDTO {
    private ScheduleServiceDTO schedule;
    private int dataSize;
    @Builder.Default private List<UserArrivalDataServiceDTO> data = new ArrayList<>();

    public static ScheduleResultServiceDTO toDto(Schedule scedule, List<UserArrivalData> userArrivalDatas) {
        ScheduleResultServiceDTO serviceDTO = ScheduleResultServiceDTO.builder()
                .schedule(ScheduleServiceDTO.toDto(scedule))
                .dataSize(userArrivalDatas.size())
                .data(UserArrivalDataServiceDTO.toDtoList(userArrivalDatas))
                .build();
        return serviceDTO;
    }
}
