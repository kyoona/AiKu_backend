package konkuk.aiku.controller.dto;

import konkuk.aiku.service.dto.AnalyticsLateServiceDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AnalyticsLateResponseDto{
    private UserSimpleResponseDto user;
    private int totalLateMinute;

    public static AnalyticsLateResponseDto toDto(AnalyticsLateServiceDto serviceDto){

        return new AnalyticsLateResponseDto(UserSimpleResponseDto.toDto(serviceDto.getUser()), serviceDto.getTotalLateMinute());
    }
}
