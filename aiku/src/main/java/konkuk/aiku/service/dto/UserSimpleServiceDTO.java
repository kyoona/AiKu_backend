package konkuk.aiku.service.dto;

import konkuk.aiku.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSimpleServiceDTO {
    private String userKaKaoId;
    private String username;
    private String phoneNumber;
    private String userImg;
    private SettingServiceDTO setting;
    private int point;
    private UserRole role;
}
