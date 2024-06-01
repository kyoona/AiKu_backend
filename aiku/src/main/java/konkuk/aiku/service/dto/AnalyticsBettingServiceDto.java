package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalyticsBettingServiceDto {
    private UserSimpleServiceDto user;
    private int winningRate;

    public static AnalyticsBettingServiceDto createDto(Users arrivalUser, int winningRate){
        return new AnalyticsBettingServiceDto(UserSimpleServiceDto.toDto(arrivalUser), winningRate);
    }
}
