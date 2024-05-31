package konkuk.aiku.service.dto;

import konkuk.aiku.controller.dto.UserBettingResultResponseDto;
import konkuk.aiku.domain.UserImgData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class UserBettingResultServiceDto {
    private Long userId;
    private String username;
    private String userImg;
    private UserImgData userImgData;
    private int profit;

    public void addProfit(int point) {
        this.profit += point;
    }

    public UserBettingResultResponseDto toResponseDto() {
        return UserBettingResultResponseDto
                .builder()
                .userId(userId)
                .username(username)
                .userImg(userImg)
                .userImgData(userImgData)
                .profit(profit)
                .build();
    }
}
