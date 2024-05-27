package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.AnalyticsLateRatingServiceDto;
import konkuk.aiku.service.dto.AnalyticsLateServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public class AnalyticsLateRatingResponseDto{

    List<AnalyticsLateResponseDto> data = new ArrayList<>();

    public static AnalyticsLateRatingResponseDto toDto(AnalyticsLateRatingServiceDto serviceDto){
        List<AnalyticsLateResponseDto> dataDto = serviceDto.getData().stream()
                .map(AnalyticsLateResponseDto::toDto)
                .toList();
        return new AnalyticsLateRatingResponseDto(dataDto);
    }
}
