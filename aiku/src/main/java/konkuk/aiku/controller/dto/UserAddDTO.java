package konkuk.aiku.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserAddDTO {
    private String username;
    private String phoneNumber;
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;
}
