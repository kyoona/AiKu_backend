package konkuk.aiku.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class BettingResultListResponseDto {
    List<UserBettingResultResponseDto> data;
}
