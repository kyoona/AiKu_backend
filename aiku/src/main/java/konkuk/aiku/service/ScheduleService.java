package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.LocationServiceDTO;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserGroupRepository userGroupRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public Long addSchedule(String kakaoId, Long groupId, ScheduleServiceDTO scheduleServiceDTO){
        Users user = findUserByKakaoId(kakaoId);
        Groups group = checkUserInGroup(user.getId(), groupId).getGroup();

        Schedule schedule = Schedule.builder()
                .scheduleName(scheduleServiceDTO.getScheduleName())
                .location(createLocation(scheduleServiceDTO.getLocation()))
                .scheduleTime(scheduleServiceDTO.getScheduleTime())
                .build();
        schedule.setGroup(group);
        schedule.addUser(user, new UserSchedule());

        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @Transactional
    public void modifySchedule(String kakaoId, Long groupId, Long scheduleId, ScheduleServiceDTO scheduleServiceDTO) {
        Users user = findUserByKakaoId(kakaoId);
        checkUserInGroup(user.getId(), groupId);

        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        if(StringUtils.hasText(scheduleServiceDTO.getScheduleName())){
            schedule.setScheduleName(scheduleServiceDTO.getScheduleName());
        }
        if(scheduleServiceDTO.getLocation() != null){
            schedule.setLocation(createLocation(scheduleServiceDTO.getLocation()));
        }
        if (scheduleServiceDTO.getScheduleTime() != null) {
            schedule.setScheduleTime(scheduleServiceDTO.getScheduleTime());
        }
        if (scheduleServiceDTO.getStatus() != null) {
            schedule.setStatus(scheduleServiceDTO.getStatus());
        }
    }

    private UserGroup checkUserInGroup(Long userId, Long groupId){
        Optional<UserGroup> userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if(userGroup.isEmpty()){
            throw new NoAthorityToAccessException();
        }
        return userGroup.get();
    }

    private Users findUserByKakaoId(String kakaoId){
        return usersRepository.findByKakaoId(kakaoId).get();
    }

    private Location createLocation(LocationServiceDTO locationServiceDTO){
        return new Location(locationServiceDTO.getLatitude(), locationServiceDTO.getLongitude(), locationServiceDTO.getLocationName());
    }

}
