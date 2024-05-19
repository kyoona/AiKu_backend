package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.UserImgData.ImgType;
import konkuk.aiku.domain.Users;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PROTECTED)
public class RealTimeLocationMessage extends Message{
    protected String title;
    protected Long scheduleId;
    protected Long userId;
    protected String userName;
    protected String userImg;
    protected ImgType imgData;
    protected String colorCode;
    protected Double latitude;
    protected Double longitude;

    public static RealTimeLocationMessage createMessage(Users user, Long scheduleId, Double latitude, Double longitude) {
        return RealTimeLocationMessage.builder()
                .title(MessageTitle.USER_REAL_TIME_LOCATION.getTitle())
                .scheduleId(scheduleId)
                .userId(user.getId())
                .userName(user.getUsername())
                .userImg(user.getUserImg())
                .imgData(user.getUserImgData().getImgData())
                .colorCode(user.getUserImgData().getColorCode())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
