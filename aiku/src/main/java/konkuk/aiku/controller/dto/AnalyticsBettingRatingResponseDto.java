package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.AnalyticsBettingServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class AnalyticsBettingRatingResponseDto {

    List<AnalyticsBettingResponseDto> data = new ArrayList<>();

    public static AnalyticsBettingRatingResponseDto toDto(List<AnalyticsBettingServiceDto> serviceDtoList) {
        List<AnalyticsBettingResponseDto> dataAnalytics = new ArrayList<>();

        for (AnalyticsBettingServiceDto serviceDto : serviceDtoList) {
            dataAnalytics.add(AnalyticsBettingResponseDto.toDto(serviceDto));
        }

        return new AnalyticsBettingRatingResponseDto(dataAnalytics);
    }

}
