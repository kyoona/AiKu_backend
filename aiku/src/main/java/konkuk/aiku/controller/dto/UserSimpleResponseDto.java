package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.UserSimpleServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserSimpleResponseDto {
    private Long userId;
    private String username;
    private String userImg;

    public static UserSimpleResponseDto toDto(UserSimpleServiceDto serviceDTO){
        UserSimpleResponseDto dto = UserSimpleResponseDto.builder()
                .userId(serviceDTO.getUserId())
                .username(serviceDTO.getUsername())
                .userImg(serviceDTO.getUserImg())
                .build();
        return dto;
    }

    public static List<UserSimpleResponseDto> toDtos(List<UserSimpleServiceDto> serviceDtos){
        List<UserSimpleResponseDto> dtos = new ArrayList<>();
        for (UserSimpleServiceDto serviceDto : serviceDtos) {
            dtos.add(toDto(serviceDto));
        }
        return dtos;
    }
}
