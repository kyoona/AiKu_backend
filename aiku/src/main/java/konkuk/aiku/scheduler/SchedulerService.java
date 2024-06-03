package konkuk.aiku.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SchedulerService {

    private final ConcurrentHashMap<SchedulerKey, ScheduledFuture<?>> scheduleList = new ConcurrentHashMap();
    private final ConcurrentHashMap<SchedulerKey, ScheduledFuture<?>> bettingList = new ConcurrentHashMap();

    //Schedule
    public void addScheduleFinishAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_FINISH_ALARM, scheduleId);
        scheduleList.put(key, scheduler);
    }

    public void addNextScheduleAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.NEXT_SCHEDULE_ALARM, scheduleId);
        scheduleList.put(key, scheduler);
    }

    public void addScheduleMapOpenAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_MAP_OPEN_ALARM, scheduleId);
        scheduleList.put(key, scheduler);
    }

    public void scheduleAutoClose(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_MAP_CLOSE, scheduleId);
        scheduleList.put(key, scheduler);
    }

    public void deleteSchedule(Long scheduleId){
        SchedulerKey nextAlarmKey = new SchedulerKey(SchedulerType.NEXT_SCHEDULE_ALARM, scheduleId);
        SchedulerKey finishAlarmKey = new SchedulerKey(SchedulerType.SCHEDULE_FINISH_ALARM, scheduleId);
        SchedulerKey openAlarmKey = new SchedulerKey(SchedulerType.SCHEDULE_MAP_OPEN_ALARM, scheduleId);
        SchedulerKey closeEventKey = new SchedulerKey(SchedulerType.SCHEDULE_MAP_CLOSE, scheduleId);

        findScheduleAndDelete(nextAlarmKey);
        findScheduleAndDelete(finishAlarmKey);
        findScheduleAndDelete(openAlarmKey);
        findScheduleAndDelete(closeEventKey);
    }

    //==Betting==
    public void bettingAcceptDelay(Long bettingId, Runnable runnable){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, 10, TimeUnit.SECONDS);

        SchedulerKey key = new SchedulerKey(SchedulerType.BETTING_ACCEPT_DELAY, bettingId);
        bettingList.put(key, scheduler);
    }

    //==편의 메서드==
    public void findScheduleAndDelete(SchedulerKey key){
        ScheduledFuture<?> scheduledFuture = scheduleList.get(key);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduleList.remove(key);
        }
    }

    public Long getTimeDelay(LocalDateTime scheduleTime){
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        ZonedDateTime now = ZonedDateTime.now(zoneId);
        ZonedDateTime getScheduleTime = ZonedDateTime.of(scheduleTime, zoneId);

        return Duration.between(now, getScheduleTime).toMinutes();
    }
}
