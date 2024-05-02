package konkuk.aiku.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SettingServiceDTO {
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;
}
