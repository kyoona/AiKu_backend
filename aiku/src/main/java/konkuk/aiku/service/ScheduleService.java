package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.AlreadyInException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static konkuk.aiku.service.dto.ServiceDtoUtils.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public Long addSchedule(Users user, Long groupId, ScheduleServiceDto scheduleServiceDTO){
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

    //TODO
    @Transactional
    public void modifySchedule(Users user, Long groupId, Long scheduleId, ScheduleServiceDto scheduleServiceDTO) {
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
    }

    @Transactional
    public void deleteSchedule(Users user, Long groupId, Long scheduleId) {
        checkUserInSchedule(user.getId(), scheduleId);

        scheduleRepository.deleteById(scheduleId);
    }

    public ScheduleDetailServiceDto findScheduleDetailById(Users user, Long groupId, Long scheduleId){
        Long userId = user.getId();
        checkUserInGroup(userId, groupId);
        Schedule schedule = findScheduleById(scheduleId);
        List<Users> waitUsers = scheduleRepository.findWaitUsersInSchedule(groupId, schedule.getUsers());
        return ScheduleDetailServiceDto.toDto(schedule, waitUsers);
    }

    @Transactional
    public void enterSchedule(Users user, Long groupId, Long scheduleId){
        Long userId = user.getId();
        checkUserInGroup(userId, groupId);
        Schedule schedule = findScheduleById(scheduleId);
        checkUserAlreadyInSchedule(userId, scheduleId);

        schedule.addUser(user, new UserSchedule());
    }

    @Transactional
    public void exitSchedule(Users user, Long groupId, Long scheduleId) {
        Long userId = user.getId();
        UserSchedule userSchedule = checkUserInSchedule(userId, scheduleId);
        Schedule schedule = userSchedule.getSchedule();
        schedule.deleteUser(user, userSchedule);
    }

    public ScheduleResultServiceDto findScheduleResult(Users user, Long groupId, Long scheduleId){
        Long userId = user.getId();
        checkUserInGroup(userId, groupId);

        Schedule schedule = findScheduleById(scheduleId);
        List<UserArrivalData> userArrivalDatas = schedule.getUserArrivalDatas();
        return ScheduleResultServiceDto.toDto(schedule, userArrivalDatas);
    }

    private UserGroup checkUserInGroup(Long userId, Long groupId){
        Optional<UserGroup> userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if(userGroup.isEmpty()){
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userGroup.get();
    }

    private UserSchedule checkUserInSchedule(Long userId, Long scheduleId){
        UserSchedule userSchedule = scheduleRepository.findByUserIdAndScheduleId(userId, scheduleId).orElse(null);
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

    private Schedule findScheduleById(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if(schedule == null){
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }
}
