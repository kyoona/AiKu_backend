package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.ScheduleStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleCond {
    String startDate;
    String endDate;
    ScheduleStatus status;
}
