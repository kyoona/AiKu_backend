package konkuk.aiku.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettingServiceDTO {
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;
}
