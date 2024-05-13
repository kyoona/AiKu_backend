package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.Users;
import lombok.Getter;

public class RealTimeLocationMessage extends Message{
    protected String title;
    protected Long userId;
    protected String userName;
    protected String userImg;
    protected Double latitude;
    protected Double longitude;

    protected RealTimeLocationMessage(Long userId, String userName, String userImg, Double latitude, Double longitude) {
        this.title = MessageTitle.USER_REAL_TIME_LOCATION.getTitle();
        this.userId = userId;
        this.userName = userName;
        this.userImg = userImg;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static RealTimeLocationMessage createMessage(Users user, Double latitude, Double longitude) {
        return new RealTimeLocationMessage(user.getId(), user.getUsername(), user.getUserImg(), latitude, longitude);
    }

}
