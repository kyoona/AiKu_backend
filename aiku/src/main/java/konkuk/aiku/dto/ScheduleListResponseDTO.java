package konkuk.aiku.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ScheduleListResponseDTO {
    private Long groupId;
    private Long scheduleSize;
    private Long pageNum;
    private int dataSize;
    private List<ScheduleSimpleResponseDTO> data;
}
