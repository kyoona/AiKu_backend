package konkuk.aiku.controller.dto;

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
}
