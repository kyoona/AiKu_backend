package konkuk.aiku.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Setting {
    private boolean isLocationInformationOn;
    private boolean isVoiceAuthorityOn;
    private boolean isBettingAlarmOn;
    private boolean isScheduleAlarmOn;
    private boolean isPinchAlarmOn;

    public Setting() {
    }

    public Setting(boolean isLocationInformationOn, boolean isVoiceAuthorityOn, boolean isBettingAlarmOn, boolean isScheduleAlarmOn, boolean isPinchAlarmOn) {
        this.isLocationInformationOn = isLocationInformationOn;
        this.isVoiceAuthorityOn = isVoiceAuthorityOn;
        this.isBettingAlarmOn = isBettingAlarmOn;
        this.isScheduleAlarmOn = isScheduleAlarmOn;
        this.isPinchAlarmOn = isPinchAlarmOn;
    }
}
