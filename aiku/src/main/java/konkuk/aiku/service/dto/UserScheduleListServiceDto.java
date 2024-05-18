package konkuk.aiku.service.dto;

import konkuk.aiku.domain.UserRole;
import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PROTECTED)
@Getter
public class UserScheduleListServiceDto {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String userImg;
    private int point;
    private UserRole role;
    private int runSchedule;
    private int waitSchedule;
    private int termSchedule;
    @Builder.Default private List<ScheduleSimpleServiceDto> data = new ArrayList<>();

    public static UserScheduleListServiceDto toDto(Users user, int runSchedule, int waitSchedule, int termSchedule, List<ScheduleSimpleServiceDto> data){
        return UserScheduleListServiceDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .userImg(user.getUserImg())
                .point(user.getPoint())
                .role(user.getRole())
                .runSchedule(runSchedule)
                .waitSchedule(waitSchedule)
                .termSchedule(termSchedule)
                .data(data)
                .build();

    }
}
