package konkuk.aiku.service.dto;

import konkuk.aiku.domain.Setting;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SettingServiceDto {
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;

    public static SettingServiceDto toDto(Setting setting){
        SettingServiceDto dto = SettingServiceDto.builder()
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .build();
        return dto;
    }
}
