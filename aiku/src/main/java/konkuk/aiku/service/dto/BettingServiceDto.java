package konkuk.aiku.service.dto;

import jakarta.persistence.*;
import konkuk.aiku.domain.Betting;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.ResultType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder @ToString
public class BettingServiceDto {
    private Long id;
    private UserSimpleServiceDto bettor;
    private UserSimpleServiceDto targetUser;
    private ScheduleServiceDto schedule;
    private int point;
    private ResultType resultType; // WIN, LOSE
    private BettingType bettingType; // RACING, BETTING

    public static BettingServiceDto toServiceDto(Betting betting) {
        UserSimpleServiceDto bettorDto = UserSimpleServiceDto.toDto(betting.getBettor());
        UserSimpleServiceDto targetDto = UserSimpleServiceDto.toDto(betting.getTargetUser());
        ScheduleServiceDto scheduleDto = ScheduleServiceDto.toDto(betting.getSchedule());

        return BettingServiceDto.builder()
                .id(betting.getId())
                .bettor(bettorDto)
                .targetUser(targetDto)
                .schedule(scheduleDto)
                .point(betting.getPoint())
                .resultType(betting.getResultType())
                .bettingType(betting.getBettingType())
                .build();
    }
}
