package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Setting;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class SettingAuthorityDTO {
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;

    public Setting toSetting(Setting setting) {
        return Setting.builder()
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isLocationInformationOn(isLocationInformationOn)
                .isVoiceAuthorityOn(isVoiceAuthorityOn)
                .build();
    }

    public static SettingAuthorityDTO toDto(Setting setting) {
        return SettingAuthorityDTO.builder()
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .build();
    }
}
