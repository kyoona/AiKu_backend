package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.UserSimpleServiceDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserSimpleResponseDTO {
    private Long userId;
    private String username;
    private String userImg;

    public static UserSimpleResponseDTO toDto(UserSimpleServiceDTO serviceDTO){
        UserSimpleResponseDTO dto = UserSimpleResponseDTO.builder()
                .userId(serviceDTO.getUserId())
                .username(serviceDTO.getUsername())
                .userImg(serviceDTO.getUserImg())
                .build();
        return dto;
    }

    public static List<UserSimpleResponseDTO> toDtos(List<UserSimpleServiceDTO> serviceDtos){
        List<UserSimpleResponseDTO> dtos = new ArrayList<>();
        for (UserSimpleServiceDTO serviceDto : serviceDtos) {
            dtos.add(toDto(serviceDto));
        }
        return dtos;
    }
}
