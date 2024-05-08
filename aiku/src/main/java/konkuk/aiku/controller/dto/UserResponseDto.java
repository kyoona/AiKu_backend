package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.UserTitle;
import konkuk.aiku.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class UserResponseDto {
    private Long userId;
    private String username;
    private String userImg;
    private String phoneNumber;
    private int point;
    private TitleResponseDto title;

    public static UserResponseDto toDto(Users users) {
        List<UserTitle> userTitles = users.getUserTitles();

        UserTitle userTitle = userTitles.stream().filter(title -> title.isUsed()).findAny()
                .orElseThrow(() -> new RuntimeException("타이틀이 존재하지 않습니다."));

        TitleResponseDto titleDto = TitleResponseDto.toDto(userTitle);

        return UserResponseDto.builder()
                .userId(users.getId())
                .username(users.getUsername())
                .userImg(users.getUserImg())
                .phoneNumber(users.getPhoneNumber())
                .point(users.getPoint())
                .title(titleDto)
                .build();

    }
}
