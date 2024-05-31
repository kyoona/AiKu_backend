package konkuk.aiku.service.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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
}