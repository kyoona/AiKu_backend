package konkuk.aiku.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SchedulerService {
    private final ConcurrentHashMap<SchedulerKey, ScheduledFuture<?>> schedulerList = new ConcurrentHashMap();

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

    public void publishScheduleMapCloseEvent(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.SCHEDULE_MAP_CLOSE_ALARM, scheduleId);
        schedulerList.put(key, scheduler);
    }

    //TODO
    public void deleteScheduleAlarm(Long scheduleId){
        SchedulerKey finishAlarmKey = new SchedulerKey(SchedulerType.SCHEDULE_FINISH_ALARM, scheduleId);
        SchedulerKey nextAlarmKey = new SchedulerKey(SchedulerType.NEXT_SCHEDULE_ALARM, scheduleId);

        schedulerList.get(finishAlarmKey).cancel(false);
        schedulerList.get(nextAlarmKey).cancel(false);
    }
}
