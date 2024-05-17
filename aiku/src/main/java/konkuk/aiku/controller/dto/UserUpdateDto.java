package konkuk.aiku.controller.dto;

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
    private Long userTitleId;

    public UserServiceDto toServiceDto() {

        UserTitleServiceDto userTitleDto = UserTitleServiceDto.builder()
                .id(userTitleId)
                .build();

        return UserServiceDto.builder()
                .username(username)
                .mainTitle(userTitleDto)
                .build();
    }
}
