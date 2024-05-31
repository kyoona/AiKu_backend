package konkuk.aiku.firebase.dto;

import konkuk.aiku.domain.Schedule;
import lombok.Builder;

import java.time.LocalDateTime;

public class ScheduleMessage extends Message{
    protected String title;
    protected Long scheduleId;
    protected String scheduleName;
    protected String locationName;
    protected LocalDateTime scheduleTime;

    public ScheduleMessage(MessageTitle messageTitle, Long scheduleId, String scheduleName, String locationName, LocalDateTime scheduleTime) {
        this.title = messageTitle.getTitle();
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.locationName = locationName;
        this.scheduleTime = scheduleTime;
    }

    public static ScheduleMessage createMessage(MessageTitle messageTitle, Schedule schedule){
        return new ScheduleMessage(messageTitle, schedule.getId(), schedule.getScheduleName(), schedule.getLocation().getLocationName(), schedule.getScheduleTime());
    }
}
