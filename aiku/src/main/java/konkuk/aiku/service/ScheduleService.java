package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.AlreadyInException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static konkuk.aiku.service.dto.ServiceDtoUtils.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupsRepository groupsRepository;

    @Transactional
    public Long addSchedule(Users user, Long groupId, ScheduleServiceDto scheduleServiceDTO){
        Groups group = findGroupById(groupId);
        checkUserInGroup(user, group);

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

    //TODO
    @Transactional
    public Long modifySchedule(Users user, Long groupId, Long scheduleId, ScheduleServiceDto scheduleServiceDTO) {
        Long userId = user.getId();
        UserSchedule userSchedule = checkUserInSchedule(userId, scheduleId);

        Schedule schedule = userSchedule.getSchedule();

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

        return schedule.getId();
    }

    @Transactional
    public Long deleteSchedule(Users user, Long groupId, Long scheduleId) {
        checkUserInSchedule(user.getId(), scheduleId);

        scheduleRepository.deleteById(scheduleId);
        return scheduleId;
    }

    public ScheduleDetailServiceDto findScheduleDetailById(Users user, Long groupId, Long scheduleId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        Schedule schedule = findScheduleById(scheduleId);
        List<Users> waitUsers = scheduleRepository.findWaitUsersInSchedule(groupId, schedule.getUsers());
        return ScheduleDetailServiceDto.toDto(schedule, waitUsers);
    }

    @Transactional
    public Long enterSchedule(Users user, Long groupId, Long scheduleId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);
        Schedule schedule = findScheduleById(scheduleId);
        checkUserAlreadyInSchedule(user.getId(), scheduleId);

        schedule.addUser(user, new UserSchedule());
        return schedule.getId();
    }

    @Transactional
    public Long exitSchedule(Users user, Long groupId, Long scheduleId) {
        Long userId = user.getId();
        UserSchedule userSchedule = checkUserInSchedule(userId, scheduleId);
        Schedule schedule = userSchedule.getSchedule();
        schedule.deleteUser(user, userSchedule);
        return schedule.getId();
    }

    public ScheduleResultServiceDto findScheduleResult(Users user, Long groupId, Long scheduleId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        Schedule schedule = findScheduleById(scheduleId);
        List<UserArrivalData> userArrivalDatas = schedule.getUserArrivalDatas();
        return ScheduleResultServiceDto.toDto(schedule, userArrivalDatas);
    }

    //==유저 도착 이벤트==
    @Transactional
    public boolean arriveUser(Users user, Long scheduleId, LocalDateTime arriveTime){
        if(checkUserAlreadyArrive(user, scheduleId)) return false;
        Schedule schedule = findScheduleById(scheduleId);
        log.info("스케줄 {}", schedule.getId());
        schedule.addUserArrivalData(user, arriveTime);
        return true;
    }

    //==편의 메서드==
    private Groups findGroupById(Long groupId){
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }

    private UserGroup checkUserInGroup(Users user, Groups groups){
        UserGroup userGroup = groupsRepository.findByUserAndGroup(user, groups).orElse(null);
        if(userGroup == null){
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userGroup;
    }

    private UserSchedule checkUserInSchedule(Long userId, Long scheduleId){
        UserSchedule userSchedule = scheduleRepository.findUserScheduleByUserIdAndScheduleId(userId, scheduleId).orElse(null);
        if (userSchedule == null) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userSchedule;
    }

    private boolean checkUserAlreadyInSchedule(Long userId, Long scheduleId){
        try {
            checkUserInSchedule(userId, scheduleId);
            throw new AlreadyInException(ErrorCode.ALREADY_IN_SCHEDULE);
        }catch (NoAthorityToAccessException e){
            return true;
        }
    }

    private boolean checkUserAlreadyArrive(Users user, Long scheduleId){
        UserArrivalData userArrivalData = scheduleRepository
                .findUserArrivalDataByUserIdAndScheduleId(user.getId(), scheduleId).orElse(null);
        if (userArrivalData == null) {
            return false;
        }
        return true;
    }

    private Schedule findScheduleById(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if(schedule == null){
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }
}
