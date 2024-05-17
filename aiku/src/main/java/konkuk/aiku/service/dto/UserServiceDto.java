package konkuk.aiku.service.dto;

import jakarta.persistence.*;
import konkuk.aiku.controller.dto.UserUpdateDto;
import konkuk.aiku.domain.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter @Builder
public class UserServiceDto {

    private Long id;
    private String username;
    private String phoneNumber;
    private String userImg;
    private Long kakaoId;
    private String password;
    private Setting setting;
    private List<UserTitleServiceDto> userTitles;
    private UserTitleServiceDto mainTitle; // 기본으로 설정한 타이틀
    private int point;
    private UserRole role;
    private String fcmToken;
    private LocalDateTime fcmTokenCreateAt;
    private String refreshToken;

    public static UserServiceDto toServiceDto(Users users) {
        List<UserTitleServiceDto> userTitleDtos = users.getUserTitles().stream()
                .map(UserTitleServiceDto::toServiceDto)
                .collect(Collectors.toList());
        UserTitleServiceDto mainTitleDto = UserTitleServiceDto.toServiceDto(users.getMainTitle());

        return UserServiceDto.builder()
                .id(users.getId())
                .username(users.getUsername())
                .phoneNumber(users.getPhoneNumber())
                .userImg(users.getUserImg())
                .kakaoId(users.getKakaoId())
                .password(users.getPassword())
                .setting(users.getSetting())
                .userTitles(userTitleDtos)
                .mainTitle(mainTitleDto)
                .point(users.getPoint())
                .role(users.getRole())
                .fcmToken(users.getFcmToken())
                .fcmTokenCreateAt(users.getFcmTokenCreateAt())
                .refreshToken(users.getRefreshToken())
                .build();
    }

    public Users toEntity() {
        List<UserTitleServiceDto> userTitleServiceDtos = Optional.ofNullable(userTitles)
                .orElse(Collections.emptyList());

        List<UserTitle> userTitleList = userTitleServiceDtos
                .stream()
                .map(UserTitleServiceDto::toEntity)
                .collect(Collectors.toList());

        UserTitle mainTitleEntity = null;
        if(!Objects.isNull(mainTitle)) {
            mainTitleEntity = mainTitle.toEntity();
        }

        return Users.builder()
                .id(id)
                .username(username)
                .phoneNumber(phoneNumber)
                .userImg(userImg)
                .kakaoId(kakaoId)
                .password(password)
                .setting(setting)
                .userTitles(userTitleList)
                .mainTitle(mainTitleEntity)
                .point(point)
                .role(role)
                .fcmToken(fcmToken)
                .fcmTokenCreateAt(fcmTokenCreateAt)
                .refreshToken(refreshToken)
                .build();
    }
}

