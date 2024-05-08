package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ScheduleListResponseDto {
    private Long groupId;
    private Long scheduleSize;
    private Long pageNum;
    private int dataSize;
    private List<ScheduleSimpleResponseDto> data;
}
