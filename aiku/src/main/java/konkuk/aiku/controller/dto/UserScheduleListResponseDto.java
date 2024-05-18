package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.UserScheduleListServiceDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class UserScheduleListResponseDto{
    private Long userId;
    private int runSchedule;
    private int waitSchedule;
    private int termSchedule;
    private List<ScheduleSimpleResponseDto> data;

    public static UserScheduleListResponseDto toDto(UserScheduleListServiceDto serviceDto) {
        return UserScheduleListResponseDto.builder()
                .userId(serviceDto.getUserId())
                .runSchedule(serviceDto.getRunSchedule())
                .waitSchedule(serviceDto.getWaitSchedule())
                .termSchedule(serviceDto.getTermSchedule())
                .data(ScheduleSimpleResponseDto.toDtos(serviceDto.getData()))
                .build();
    }
}

