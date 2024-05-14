package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserRole;
import konkuk.aiku.domain.Users;
import konkuk.aiku.service.dto.UserServiceDto;
import konkuk.aiku.service.dto.UserSimpleServiceDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Builder
public class UserAddDto {
    private String username;
    private String phoneNumber;
    private Long kakaoId;
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;

    public static UserAddDto toDto(Users users) {
        Setting setting = users.getSetting();

        return UserAddDto.builder()
                .username(users.getUsername())
                .phoneNumber(users.getPhoneNumber())
                .kakaoId(users.getKakaoId())
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .build();
    }

    public UserServiceDto toServiceDto() {
        Setting setting = Setting.builder()
                .isLocationInformationOn(isLocationInformationOn)
                .isVoiceAuthorityOn(isVoiceAuthorityOn)
                .isBettingAlarmOn(isBettingAlarmOn)
                .isScheduleAlarmOn(isScheduleAlarmOn)
                .isPinchAlarmOn(isPinchAlarmOn)
                .build();

        return UserServiceDto.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .kakaoId(kakaoId)
                .setting(setting)
                .build();
    }

    public Users toEntity() {
        Setting setting = new Setting(
                isLocationInformationOn,
                isVoiceAuthorityOn,
                isBettingAlarmOn,
                isScheduleAlarmOn,
                isPinchAlarmOn
        );

        return Users.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .kakaoId(kakaoId)
                .setting(setting)
                .point(0)
                .role(UserRole.USER)
                .build();

    }
}
