package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.GroupDetailServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GroupResponseDto {
    private Long groupId;
    private String groupName;
    private String description;
    @Builder.Default private List<UserSimpleResponseDto> users = new ArrayList<>();

    public static GroupResponseDto toDto(GroupDetailServiceDto serviceDto) {
        GroupResponseDto dto = GroupResponseDto.builder()
                .groupId(serviceDto.getGroupId())
                .groupName(serviceDto.getGroupName())
                .description(serviceDto.getDescription())
                .users(UserSimpleResponseDto.toDtos(serviceDto.getUsers()))
                .build();
        return dto;
    }
}
