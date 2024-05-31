package konkuk.aiku.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class SchedulerServiceTest {

    @Autowired
    private SchedulerService schedulerService;

    @Test
    void getTimeDelay() {
        Long timeDelay = schedulerService.getTimeDelay(LocalDateTime.of(2024, 5, 31, 21, 16));
        log.info("timeDelay = {}" ,timeDelay);
    }

    @Test
    public void addScheduleMapOpenAlarm() {
        //when
        schedulerService.addScheduleMapOpenAlarm(1l, () -> log.info("schedulerTest"), 1l);

        //then
        schedulerService.deleteSchedule(1l);
        ConcurrentHashMap<SchedulerKey, ScheduledFuture<?>> schedulerList = schedulerService.getSchedulerList();
        for (SchedulerKey key : schedulerList.keySet()) {
            log.info("key ={} / {} ", key.getScheduleId(), key.getType());
        }
    }
}