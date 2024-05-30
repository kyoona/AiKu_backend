package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.Schedule;
import konkuk.aiku.domain.UserImgData.ImgType;
import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
//@Builder(access = AccessLevel.PROTECTED)
public class UserArrivalMessage extends Message{
    protected String title;
    protected Long scheduleId;
    protected String scheduleName;

    protected Long userId;
    protected String userName;
    protected String userImg;
    protected ImgType imgData;
    protected String colorCode;

    protected LocalDateTime arrivalTime;

    public static UserArrivalMessage createMessage(Users user, Schedule schedule, LocalDateTime arrivalTime) {
        return UserArrivalMessage.builder()
                .title(MessageTitle.USER_SCHEDULE_ARRIVAL.getTitle())
                .scheduleId(schedule.getId())
                .scheduleName(schedule.getScheduleName())
                .userId(user.getId())
                .userName(user.getUsername())
                .userImg(user.getUserImg())
                .imgData(user.getUserImgData().getImgData())
                .colorCode(user.getUserImgData().getColorCode())
                .arrivalTime(arrivalTime)
                .build();
    }
}
