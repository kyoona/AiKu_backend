package konkuk.aiku.firebase.dto;

public enum MessageTitle {
    USER_SCHEDULE_ARRIVAL("USER_SCHEDULE_ARRIVAL"),
    USER_REAL_TIME_LOCATION("USER_REAL_TIME_LOCATION"),
    EMOJI_TO_USER("EMOJI_TO_USER"),
    SCHEDULE_FINISH("SCHEDULE_FINISH"),
    SCHEDULE_MAP_OPEN("SCHEDULE_MAP_OPEN"),
    SCHEDULE_MAP_CLOSE("SCHEDULE_MAP_CLOSE"),
    NEXT_SCHEDULE("NEXT_SCHEDULE");

    private String title;

    MessageTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
