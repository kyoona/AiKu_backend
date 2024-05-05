package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserRole;
import konkuk.aiku.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@Builder
public class UserAddDTO {
    private String username;
    private String phoneNumber;
    private String kakaoId;
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;

    public static UserAddDTO toDto(Users users) {
        Setting setting = users.getSetting();

        return UserAddDTO.builder()
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
