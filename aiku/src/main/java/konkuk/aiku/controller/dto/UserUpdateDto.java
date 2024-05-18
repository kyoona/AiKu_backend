package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.UserImgData;
import konkuk.aiku.service.dto.TitleServiceDto;
import konkuk.aiku.service.dto.UserServiceDto;
import konkuk.aiku.service.dto.UserTitleServiceDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter
public class UserUpdateDto {
    private String username;
    private String userImg;
    private UserImgData userImgData;

    public UserServiceDto toServiceDto() {

        return UserServiceDto.builder()
                .username(username)
                .userImg(userImg)
                .userImgData(userImgData)
                .build();
    }
}
