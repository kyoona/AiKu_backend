package konkuk.aiku.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
    private boolean isLocationInformationOn;
    private boolean isVoiceAuthorityOn;
    private boolean isBettingAlarmOn;
    private boolean isScheduleAlarmOn;
    private boolean isPinchAlarmOn;
}
