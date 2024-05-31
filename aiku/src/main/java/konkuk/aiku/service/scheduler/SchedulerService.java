package konkuk.aiku.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SchedulerService {
    private final ConcurrentHashMap<SchedulerKey, ScheduledFuture<?>> schedulerList = new ConcurrentHashMap();

    //Schedule
    public void addScheduleFinishAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_FINISH_ALARM, scheduleId);
        schedulerList.put(key, scheduler);
    }

    public void addNextScheduleAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.NEXT_SCHEDULE_ALARM, scheduleId);
        schedulerList.put(key, scheduler);
    }

    public void addScheduleMapOpenAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_MAP_OPEN_ALARM, scheduleId);
        schedulerList.put(key, scheduler);
    }

    public void scheduleAutoClose(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_MAP_CLOSE, scheduleId);
        schedulerList.put(key, scheduler);
    }

    public void deleteSchedule(Long scheduleId){
        SchedulerKey nextAlarmKey = new SchedulerKey(SchedulerType.NEXT_SCHEDULE_ALARM, scheduleId);
        SchedulerKey finishAlarmKey = new SchedulerKey(SchedulerType.SCHEDULE_FINISH_ALARM, scheduleId);
        SchedulerKey openAlarmKey = new SchedulerKey(SchedulerType.SCHEDULE_MAP_OPEN_ALARM, scheduleId);
        SchedulerKey closeEventKey = new SchedulerKey(SchedulerType.SCHEDULE_MAP_CLOSE, scheduleId);

        schedulerList.get(nextAlarmKey).cancel(false);
        schedulerList.get(finishAlarmKey).cancel(false);
        schedulerList.get(openAlarmKey).cancel(false);
        schedulerList.get(closeEventKey).cancel(false);
    }

    //Betting
    public void bettingAcceptDelay(Long bettingId, Runnable runnable){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, 10, TimeUnit.SECONDS);

        SchedulerKey key = new SchedulerKey(SchedulerType.BETTING_ACCEPT_DELAY, bettingId);
        schedulerList.put(key, scheduler);
    }

    //==편의 메서드==
    public Long getTimeDelay(LocalDateTime scheduleTime){
        return Duration.between(LocalDateTime.now(), scheduleTime).toSeconds();
    }
}
