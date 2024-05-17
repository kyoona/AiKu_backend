package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Groups;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GroupSimpleServiceDto {
    private Long groupId;
    private String groupName;
    private String description;
    private int memberSize;
    private LocalDateTime lastScheduleTime;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static GroupSimpleServiceDto toDto(Groups group, int memberSize, LocalDateTime lastScheduleTime){
        return GroupSimpleServiceDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .memberSize(memberSize)
                .lastScheduleTime(lastScheduleTime)
                .createdAt(group.getCreatedAt())
                .modifiedAt(group.getModifiedAt())
                .build();
    }
}
