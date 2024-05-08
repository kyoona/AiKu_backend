package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.GroupDetailServiceDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupResponseDTO {
    private Long groupId;
    private String groupName;
    private String description;
    @Builder.Default private List<UserSimpleResponseDTO> users = new ArrayList<>();

    public static GroupResponseDTO toDto(GroupDetailServiceDTO serviceDto) {
        GroupResponseDTO dto = GroupResponseDTO.builder()
                .groupId(serviceDto.getGroupId())
                .groupName(serviceDto.getGroupName())
                .description(serviceDto.getDescription())
                .users(UserSimpleResponseDTO.toDtos(serviceDto.getUsers()))
                .build();
        return dto;
    }
}
