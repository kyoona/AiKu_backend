package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.ScheduleStatus;
import lombok.Getter;

@Getter
public class ScheduleCond {
    String startDate;
    String endDate;
    ScheduleStatus status;
}
