package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.GroupSimpleServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupSimpleResponseDto {
    private Long groupId;
    private String groupName;
    private int memberSize;
    private LocalDateTime lastScheduleTime;

    public static List<GroupSimpleResponseDto> toDtos(List<GroupSimpleServiceDto> serviceDtos){
        List<GroupSimpleResponseDto> dtos = new ArrayList<>();
        serviceDtos.stream().forEach((serviceDto) -> dtos.add(toDto(serviceDto)));
        return dtos;
    }

    public static GroupSimpleResponseDto toDto(GroupSimpleServiceDto serviceDto){
        return GroupSimpleResponseDto.builder()
                .groupId(serviceDto.getGroupId())
                .groupName(serviceDto.getGroupName())
                .memberSize(serviceDto.getMemberSize())
                .lastScheduleTime(serviceDto.getLastScheduleTime())
                .build();
    }
}
