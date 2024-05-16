package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.service.dto.BettingServiceDto;
import konkuk.aiku.service.dto.UserServiceDto;
import konkuk.aiku.service.dto.UserSimpleServiceDto;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BettingAddDto {
    private Long targetUserId;
    private int point;
    private BettingType bettingType;

    public BettingServiceDto toServiceDto() {
        UserSimpleServiceDto target = UserSimpleServiceDto.builder().userId(targetUserId).build();
        return BettingServiceDto.builder()
                .targetUser(target)
                .point(point)
                .bettingType(bettingType)
                .build();
    }
}
