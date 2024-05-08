package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserArrivalData;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ScheduleResultServiceDto {
    private ScheduleServiceDto schedule;
    private int dataSize;
    @Builder.Default private List<UserArrivalDataServiceDto> data = new ArrayList<>();

    public static ScheduleResultServiceDto toDto(Schedule scedule, List<UserArrivalData> userArrivalDatas) {
        ScheduleResultServiceDto serviceDTO = ScheduleResultServiceDto.builder()
                .schedule(ScheduleServiceDto.toDto(scedule))
                .dataSize(userArrivalDatas.size())
                .data(UserArrivalDataServiceDto.toDtoList(userArrivalDatas))
                .build();
        return serviceDTO;
    }
}
