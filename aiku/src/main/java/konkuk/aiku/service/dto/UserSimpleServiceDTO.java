package konkuk.aiku.service.dto;

import konkuk.aiku.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserSimpleServiceDTO {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String userImg;
    private SettingServiceDTO setting;
    private int point;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
