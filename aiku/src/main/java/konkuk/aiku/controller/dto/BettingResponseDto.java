package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.ResultType;
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

    public static BettingResponseDto toDto(Betting betting) {
        // Service DTO로 수정 필요
        UserSimpleServiceDto bettorServiceDto = UserSimpleServiceDto.toDto(betting.getBettor());
        UserSimpleServiceDto targetServiceDto = UserSimpleServiceDto.toDto(betting.getTargetUser());

        UserSimpleResponseDto bettorDto = UserSimpleResponseDto.toDto(bettorServiceDto);
        UserSimpleResponseDto targetDto = UserSimpleResponseDto.toDto(targetServiceDto);

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
