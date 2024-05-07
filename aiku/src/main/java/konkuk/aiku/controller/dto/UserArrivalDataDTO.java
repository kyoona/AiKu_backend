package konkuk.aiku.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserArrivalDataDTO {
    private Long userArrivalDataId;
    private UserSimpleResponseDTO user;
    private int timeDifference;
}
