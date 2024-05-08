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
public class UserArrivalDataServiceDTO {
    private Long userArrivalDataId;
    private UserSimpleServiceDTO user;
    private LocalDateTime arrivalTime;
    private int timeDifference;
    private Location startLocation;
    private Location endLocation;

    public static List<UserArrivalDataServiceDTO> toDtoList(List<UserArrivalData> userArrivalDatas){
        List<UserArrivalDataServiceDTO> dtos = new ArrayList<>();
        for (UserArrivalData userArrivalData : userArrivalDatas) {
            dtos.add(toDto(userArrivalData));
        }
        return dtos;
    }

    public static UserArrivalDataServiceDTO toDto(UserArrivalData userArrivalData) {
        UserArrivalDataServiceDTO dto = UserArrivalDataServiceDTO.builder()
                .userArrivalDataId(userArrivalData.getId())
                .user(UserSimpleServiceDTO.toDto(userArrivalData.getUser()))
                .arrivalTime(userArrivalData.getArrivalTime())
                .timeDifference(userArrivalData.getTimeDifference())
                .startLocation(userArrivalData.getStartLocation())
                .endLocation(userArrivalData.getEndLocation())
                .build();
        return dto;
    }
}
