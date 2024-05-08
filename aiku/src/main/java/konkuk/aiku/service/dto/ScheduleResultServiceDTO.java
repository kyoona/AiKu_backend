package konkuk.aiku.service.dto;

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
}
