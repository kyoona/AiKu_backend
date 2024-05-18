package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserImgData;
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
    private String userImg;
    private UserImgData userImgData;
    private Boolean isLocationInformationOn;
    private Boolean isVoiceAuthorityOn;
    private Boolean isBettingAlarmOn;
    private Boolean isScheduleAlarmOn;
    private Boolean isPinchAlarmOn;

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
                .userImg(userImg)
                .userImgData(userImgData)
                .setting(setting)
                .build();
    }
}
