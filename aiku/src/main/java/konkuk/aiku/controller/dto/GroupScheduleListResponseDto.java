package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.GroupScheduleListServiceDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class GroupScheduleListResponseDto {
    private Long groupId;
    private int runSchedule;
    private int waitSchedule;
    private int termSchedule;
    private List<ScheduleSimpleResponseDto> data;

    public static GroupScheduleListResponseDto toDto(GroupScheduleListServiceDto serviceDto){
        return GroupScheduleListResponseDto.builder()
                .groupId(serviceDto.getGroupId())
                .runSchedule(serviceDto.getRunSchedule())
                .waitSchedule(serviceDto.getWaitSchedule())
                .termSchedule(serviceDto.getTermSchedule())
                .data(ScheduleSimpleResponseDto.toDtos(serviceDto.getData()))
                .build();
    }
}
