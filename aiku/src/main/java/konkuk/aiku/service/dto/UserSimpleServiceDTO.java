package konkuk.aiku.service.dto;

import konkuk.aiku.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSimpleServiceDTO {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String userImg;
    private SettingServiceDTO setting;
    private int point;
    private UserRole role;
}
