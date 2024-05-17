package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.BettingServiceDto;
import konkuk.aiku.service.dto.UserSimpleServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class BettingModifyDto {
    private Long targetUserId;
    private int point;

    public BettingServiceDto toServiceDto() {
        UserSimpleServiceDto targetUser = UserSimpleServiceDto.builder().userId(targetUserId).build();

        return BettingServiceDto.builder()
                .targetUser(targetUser)
                .point(point)
                .build();
    }
}
