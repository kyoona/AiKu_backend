package konkuk.aiku.service.dto;

import konkuk.aiku.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserSimpleServiceDto {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String userImg;
    private UserImgData userImgData;
    private SettingServiceDto setting;
    private int point;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserSimpleServiceDto toDto(Users user){
        UserSimpleServiceDto dto = UserSimpleServiceDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .userImg(user.getUserImg())
                .userImgData(user.getUserImgData())
                .setting(SettingServiceDto.toDto(user.getSetting()))
                .point(user.getPoint())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
        return dto;
    }

    public static List<UserSimpleServiceDto> toDtosByUser(List<Users> users){
        List<UserSimpleServiceDto> dtos = new ArrayList<>();
        for (Users user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    public static List<UserSimpleServiceDto> toDtosByUserSchedule(List<UserSchedule> users){
        List<UserSimpleServiceDto> dtos = new ArrayList<>();
        for (UserSchedule userSchedule : users) {
            Users user = userSchedule.getUser();
            dtos.add(toDto(user));
        }
        return dtos;
    }

    public static List<UserSimpleServiceDto> toDtoListByUserGroup(List<UserGroup> userGroups){
        List<UserSimpleServiceDto> dtos = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            dtos.add(UserSimpleServiceDto.toDto(userGroup.getUser()));
        }
        return dtos;
    }
}
