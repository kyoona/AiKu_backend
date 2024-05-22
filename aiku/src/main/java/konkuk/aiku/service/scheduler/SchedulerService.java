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

    public void addCurrentScheduleAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.CURRENT_SHCEDULE_ALARM, scheduleId);
        schedulerList.put(key, scheduler);
    }

    public void addNextScheduleAlarm(Long scheduleId, Runnable runnable, Long delayMinutes){
        ScheduledFuture<?> scheduler = Executors.newScheduledThreadPool(1).schedule(runnable, delayMinutes, TimeUnit.MINUTES);

        SchedulerKey key = new SchedulerKey(SchedulerType.CURRENT_SHCEDULE_ALARM, scheduleId);
        schedulerList.put(key, scheduler);
    }

    public void deleteScheduleAlarm(Long scheduleId){
        SchedulerKey currentAlarmKey = new SchedulerKey(SchedulerType.CURRENT_SHCEDULE_ALARM, scheduleId);
        SchedulerKey nextAlarmKey = new SchedulerKey(SchedulerType.NEXT_SCHEDULE_ALARM, scheduleId);

        schedulerList.get(currentAlarmKey).cancel(false);
        schedulerList.get(nextAlarmKey).cancel(false);
    }
}
