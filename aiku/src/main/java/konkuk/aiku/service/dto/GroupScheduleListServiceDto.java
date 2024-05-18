package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Groups;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class GroupScheduleListServiceDto {
    private Long groupId;
    private String groupName;
    private String description;
    private int runSchedule;
    private int waitSchedule;
    private int termSchedule;
    @Builder.Default private List<ScheduleSimpleServiceDto> data = new ArrayList<>();

    public static GroupScheduleListServiceDto toDto(Groups group, int runSchedule, int waitSchedule, int termSchedule, List<ScheduleSimpleServiceDto> data){
        return GroupScheduleListServiceDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .runSchedule(runSchedule)
                .waitSchedule(waitSchedule)
                .termSchedule(termSchedule)
                .data(data)
                .build();
    }
}
