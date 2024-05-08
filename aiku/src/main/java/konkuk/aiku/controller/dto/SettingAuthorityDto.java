package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Setting;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class SettingAuthorityDto {
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

    public static SettingAuthorityDto toDto(Setting setting) {
        return SettingAuthorityDto.builder()
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .build();
    }
}
