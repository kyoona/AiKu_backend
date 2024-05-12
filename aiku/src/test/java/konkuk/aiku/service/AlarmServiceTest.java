package konkuk.aiku.service;

import konkuk.aiku.controller.dto.RealTimeLocationDto;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AlarmServiceTest {

    @Autowired
    AlarmService alarmService;
    @Autowired
    UsersRepository usersRepository;

    @Test
    void sendLocationInSchedule() {
        Users user = usersRepository.findById(25l).get();
        alarmService.sendLocationInSchedule(user, 7l, new RealTimeLocationDto(127.1, 127.1));
    }
}