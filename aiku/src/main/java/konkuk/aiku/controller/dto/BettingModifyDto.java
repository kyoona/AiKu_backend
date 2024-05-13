package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BettingModifyDto {
    private Long targetUserId;
    private int point;
}
