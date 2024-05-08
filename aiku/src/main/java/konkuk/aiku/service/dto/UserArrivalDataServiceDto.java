package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Location;
import konkuk.aiku.domain.UserArrivalData;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserArrivalDataServiceDto {
    private Long userArrivalDataId;
    private UserSimpleServiceDto user;
    private LocalDateTime arrivalTime;
    private int timeDifference;
    private Location startLocation;
    private Location endLocation;

    public static List<UserArrivalDataServiceDto> toDtoList(List<UserArrivalData> userArrivalDatas){
        List<UserArrivalDataServiceDto> dtos = new ArrayList<>();
        for (UserArrivalData userArrivalData : userArrivalDatas) {
            dtos.add(toDto(userArrivalData));
        }
        return dtos;
    }

    public static UserArrivalDataServiceDto toDto(UserArrivalData userArrivalData) {
        UserArrivalDataServiceDto dto = UserArrivalDataServiceDto.builder()
                .userArrivalDataId(userArrivalData.getId())
                .user(UserSimpleServiceDto.toDto(userArrivalData.getUser()))
                .arrivalTime(userArrivalData.getArrivalTime())
                .timeDifference(userArrivalData.getTimeDifference())
                .startLocation(userArrivalData.getStartLocation())
                .endLocation(userArrivalData.getEndLocation())
                .build();
        return dto;
    }
}
