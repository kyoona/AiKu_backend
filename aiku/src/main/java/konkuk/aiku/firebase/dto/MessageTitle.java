package konkuk.aiku.firebase.dto;

public enum MessageTitle {
    USER_SCHEDULE_ARRIVAL("USER_SCHEDULE_ARRIVAL"),
    USER_REAL_TIME_LOCATION("USER_REAL_TIME_LOCATION");

    private String title;

    MessageTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
