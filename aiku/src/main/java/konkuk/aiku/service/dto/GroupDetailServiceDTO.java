package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Groups;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupDetailServiceDTO {
    private Long groupId;
    private String groupName;
    private String groupImg;
    private String description;
    @Builder.Default private List<UserSimpleServiceDTO> users = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static GroupDetailServiceDTO toDto(Groups group, List<UserSimpleServiceDTO> userSimpleServiceDTOs){
        GroupDetailServiceDTO groupDetailServiceDTO = GroupDetailServiceDTO.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupImg(group.getGroupImg())
                .description(group.getDescription())
                .users(userSimpleServiceDTOs)
                .createdAt(group.getCreatedAt())
                .modifiedAt(group.getModifiedAt())
                .build();
        return groupDetailServiceDTO;
    }
}
