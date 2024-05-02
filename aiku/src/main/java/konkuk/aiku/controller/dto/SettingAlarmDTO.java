package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SettingAlarmDTO {
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;
}
