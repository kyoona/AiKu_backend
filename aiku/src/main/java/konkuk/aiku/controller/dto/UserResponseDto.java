package konkuk.aiku.controller.dto;

import konkuk.aiku.domain.Title;
import konkuk.aiku.domain.UserTitle;
import konkuk.aiku.domain.Users;
import konkuk.aiku.service.dto.TitleServiceDto;
import konkuk.aiku.service.dto.UserServiceDto;
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
        TitleResponseDto titleDto = TitleResponseDto.toDto(users.getMainTitle());

        return UserResponseDto.builder()
                .userId(users.getId())
                .username(users.getUsername())
                .userImg(users.getUserImg())
                .phoneNumber(users.getPhoneNumber())
                .point(users.getPoint())
                .title(titleDto)
                .build();

    }

    public static UserServiceDto toServiceDto(Users users) {
        TitleServiceDto titleDto = TitleServiceDto.toDto(users.getMainTitle());

        return UserServiceDto.builder()
                .id(users.getId())
                .username(users.getUsername())
                .userImg(users.getUserImg())
                .phoneNumber(users.getPhoneNumber())
                .point(users.getPoint())
                .mainTitle(titleDto)
                .build();

    }
}
