package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

public class UserArrivalMessage extends Message{
    private String title;

    private Long userId;
    private String userName;
    private String userImg;

    private Long scheduleId;
    private String scheduleName;

    private LocalDateTime arrivalTime;


    @Builder(access = AccessLevel.PRIVATE)
    protected UserArrivalMessage(Long userId, String userName, String userImg, Long scheduleId, String scheduleName, LocalDateTime arrivalTime) {
        this.title = MessageTitle.USER_SCHEDULE_ARRIVAL.getTitle();
        this.userId = userId;
        this.userName = userName;
        this.userImg = userImg;
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.arrivalTime = arrivalTime;
    }

    public static UserArrivalMessage createMessage(Users user, Schedule schedule, LocalDateTime arrivalTime) {
        return UserArrivalMessage.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .userImg(user.getUserImg())
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .build();
    }
}
