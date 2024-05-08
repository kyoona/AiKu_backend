package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Groups;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupDetailServiceDto {
    private Long groupId;
    private String groupName;
    private String groupImg;
    private String description;
    @Builder.Default private List<UserSimpleServiceDto> users = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static GroupDetailServiceDto toDto(Groups group, List<UserSimpleServiceDto> userSimpleServiceDtos){
        GroupDetailServiceDto groupDetailServiceDTO = GroupDetailServiceDto.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupImg(group.getGroupImg())
                .description(group.getDescription())
                .users(userSimpleServiceDtos)
                .createdAt(group.getCreatedAt())
                .modifiedAt(group.getModifiedAt())
                .build();
        return groupDetailServiceDTO;
    }
}
