package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Location;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserArrivalDataServiceDTO {
    private Long userArrivalDataId;
    private UserSimpleServiceDTO user;
    private LocalDateTime arrivalTime;
    private int timeDifference;
    private Location startLocation;
    private Location endLocation;
}
