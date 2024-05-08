package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Setting;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class SettingAlarmDto {
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;

    public Setting toSetting(Setting setting) {
        return Setting.builder()
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .isBettingAlarmOn(isBettingAlarmOn)
                .isScheduleAlarmOn(isScheduleAlarmOn)
                .isPinchAlarmOn(isPinchAlarmOn)
                .build();
    }

    public static SettingAlarmDto toDto(Setting setting) {
        return SettingAlarmDto.builder()
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .build();
    }
}
