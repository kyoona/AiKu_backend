package konkuk.aiku.service.dto;

import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.UserRole;
import konkuk.aiku.domain.UserSchedule;
import konkuk.aiku.domain.Users;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static UserSimpleServiceDTO toDto(Users user){
        UserSimpleServiceDTO dto = UserSimpleServiceDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .userImg(user.getUserImg())
                .setting(SettingServiceDTO.toDto(user.getSetting()))
                .point(user.getPoint())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
        return dto;
    }

    public static List<UserSimpleServiceDTO> toDtosByUser(List<Users> users){
        List<UserSimpleServiceDTO> dtos = new ArrayList<>();
        for (Users user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    public static List<UserSimpleServiceDTO> toDtosByUserSchedule(List<UserSchedule> users){
        List<UserSimpleServiceDTO> dtos = new ArrayList<>();
        for (UserSchedule userSchedule : users) {
            Users user = userSchedule.getUser();
            dtos.add(toDto(user));
        }
        return dtos;
    }

    public static List<UserSimpleServiceDTO> toDtoListByUserGroup(List<UserGroup> userGroups){
        List<UserSimpleServiceDTO> dtos = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            dtos.add(UserSimpleServiceDTO.toDto(userGroup.getUser()));
        }
        return dtos;
    }
}
