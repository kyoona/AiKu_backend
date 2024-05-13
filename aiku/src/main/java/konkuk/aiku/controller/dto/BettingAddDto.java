package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BettingAddDto {
    private Long targetUserId;
    private int point;
    private BettingType bettingType;
}
