package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.ScheduleListServiceDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class ScheduleListResponseDto {
    private Long groupId;
    private int runSchedule;
    private int waitSchedule;
    private int termSchedule;
    private List<ScheduleSimpleResponseDto> data;

    public static ScheduleListResponseDto toDto(ScheduleListServiceDto serviceDto){
        return ScheduleListResponseDto.builder()
                .groupId(serviceDto.getGroupId())
                .runSchedule(serviceDto.getRunSchedule())
                .waitSchedule(serviceDto.getWaitSchedule())
                .termSchedule(serviceDto.getTermSchedule())
                .data(ScheduleSimpleResponseDto.toDtos(serviceDto.getData()))
                .build();
    }
}
