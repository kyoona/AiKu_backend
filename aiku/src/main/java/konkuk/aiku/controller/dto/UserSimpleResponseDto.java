package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.UserImgData;
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
    private UserImgData userImgData;
    private int point;

    public static UserSimpleResponseDto toDto(UserSimpleServiceDto serviceDTO){
        UserSimpleResponseDto dto = UserSimpleResponseDto.builder()
                .userId(serviceDTO.getUserId())
                .username(serviceDTO.getUsername())
                .userImg(serviceDTO.getUserImg())
                .userImgData(serviceDTO.getUserImgData())
                .point(serviceDTO.getPoint())
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
