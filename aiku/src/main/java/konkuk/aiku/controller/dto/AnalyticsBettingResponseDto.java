package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.AnalyticsBettingServiceDto;
import konkuk.aiku.service.dto.AnalyticsLateServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnalyticsBettingResponseDto {
    private UserSimpleResponseDto user;
    private int winningRate;

    public static AnalyticsBettingResponseDto toDto(AnalyticsBettingServiceDto serviceDto) {
        UserSimpleResponseDto userDto = UserSimpleResponseDto.toDto(serviceDto.getUser());

        return AnalyticsBettingResponseDto
                .builder()
                .user(userDto)
                .winningRate(serviceDto.getWinningRate())
                .build();
    }
}
