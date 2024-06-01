package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.UserImgData;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBettingResultResponseDto {
    private Long userId;
    private String username;
    private String userImg;
    private UserImgData userImgData;
    private int profit;
}
