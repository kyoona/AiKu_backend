package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalyticsLateServiceDto {
    private UserSimpleServiceDto user;
    private int totalLateMinute;

    public static AnalyticsLateServiceDto createDto(Users arrivalUser, Integer totalLateTime){
        return new AnalyticsLateServiceDto(UserSimpleServiceDto.toDto(arrivalUser), totalLateTime);
    }
}
