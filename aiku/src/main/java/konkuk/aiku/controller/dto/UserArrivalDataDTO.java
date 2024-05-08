package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.UserArrivalDataServiceDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class UserArrivalDataDTO {
    private Long userArrivalDataId;
    private UserSimpleResponseDTO user;
    private int timeDifference;

    public static List<UserArrivalDataDTO> toDtoList(List<UserArrivalDataServiceDTO> serviceDtos){
        List<UserArrivalDataDTO> dto = new ArrayList<>();
        for (UserArrivalDataServiceDTO serviceDto : serviceDtos) {
            dto.add(toDto(serviceDto));
        }
        return dto;
    }

    public static UserArrivalDataDTO toDto(UserArrivalDataServiceDTO serviceDto){
        UserArrivalDataDTO dto = UserArrivalDataDTO.builder()
                .userArrivalDataId(serviceDto.getUserArrivalDataId())
                .user(createUserSimpleServiceDTO(serviceDto.getUser()))
                .timeDifference(serviceDto.getTimeDifference())
                .build();
        return dto;
    }
}
