package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.GroupListServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class GroupListResponseDto {
    private Long userId;
    private List<GroupSimpleResponseDto> data;

    public static GroupListResponseDto toDto(GroupListServiceDto serviceDto) {
        List<GroupSimpleResponseDto> data = GroupSimpleResponseDto.toDtos(serviceDto.getData());
        return new GroupListResponseDto(serviceDto.getUserId(), data);
    }
}
