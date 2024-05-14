package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.ResultType;
import konkuk.aiku.service.dto.BettingServiceDto;
import konkuk.aiku.service.dto.UserSimpleServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class BettingResponseDto {
    private Long bettingId;
    private UserSimpleResponseDto bettor;
    private UserSimpleResponseDto targetUser;
    private int point;
    private BettingType bettingType;
    private ResultType resultType;

    public static BettingResponseDto toDto(BettingServiceDto betting) {
        // Service DTO로 수정 필요
        UserSimpleResponseDto bettorDto = UserSimpleResponseDto.toDto(betting.getBettor());
        UserSimpleResponseDto targetDto = UserSimpleResponseDto.toDto(betting.getTargetUser());

        return BettingResponseDto.builder()
                .bettingId(betting.getId())
                .bettor(bettorDto)
                .targetUser(targetDto)
                .point(betting.getPoint())
                .bettingType(betting.getBettingType())
                .resultType(betting.getResultType())
                .build();
    }
}
