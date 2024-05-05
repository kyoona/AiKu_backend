package konkuk.aiku.service;

import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Long scheduleAdd(String kakaoId, Long groupId, ScheduleServiceDTO scheduleServiceDTO){

    }
}
