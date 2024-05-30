package konkuk.aiku.firebase.dto;

public enum MessageTitle {
    //스케줄
    NEXT_SCHEDULE("NEXT_SCHEDULE"),
    SCHEDULE_FINISH("SCHEDULE_FINISH"),
    SCHEDULE_MAP_OPEN("SCHEDULE_MAP_OPEN"),
    SCHEDULE_MAP_CLOSE("SCHEDULE_MAP_CLOSE"),

    //맵 정보
    USER_SCHEDULE_ARRIVAL("USER_SCHEDULE_ARRIVAL"),
    USER_REAL_TIME_LOCATION("USER_REAL_TIME_LOCATION"),
    EMOJI_TO_USER("EMOJI_TO_USER"),

    //베팅
    BETTING_START("BETTING_START"),
    BETTING_FINISH("BETTING_FINISH"),
    BETTING_NEW("BETTING_NEW"),
    BETTING_ACCEPT("BETTING_ACCEPT"),
    BETTING_DENY("BETTING_DENY"),

    //칭호
    TITLE_OPEN("TITLE_OPEN");



    private String title;

    MessageTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
