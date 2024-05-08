package konkuk.aiku.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setting {
    private boolean isLocationInformationOn;
    private boolean isVoiceAuthorityOn;
    private boolean isBettingAlarmOn;
    private boolean isScheduleAlarmOn;
    private boolean isPinchAlarmOn;
}
