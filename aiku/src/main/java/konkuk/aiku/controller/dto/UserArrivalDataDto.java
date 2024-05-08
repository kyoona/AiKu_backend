package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.UserArrivalDataServiceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class UserArrivalDataDto {
    private Long userArrivalDataId;
    private UserSimpleResponseDto user;
    private int timeDifference;

    public static List<UserArrivalDataDto> toDtoList(List<UserArrivalDataServiceDto> serviceDtos){
        List<UserArrivalDataDto> dto = new ArrayList<>();
        for (UserArrivalDataServiceDto serviceDto : serviceDtos) {
            dto.add(toDto(serviceDto));
        }
        return dto;
    }

    public static UserArrivalDataDto toDto(UserArrivalDataServiceDto serviceDto){
        UserArrivalDataDto dto = UserArrivalDataDto.builder()
                .userArrivalDataId(serviceDto.getUserArrivalDataId())
                .user(UserSimpleResponseDto.toDto(serviceDto.getUser()))
                .timeDifference(serviceDto.getTimeDifference())
                .build();
        return dto;
    }
}
