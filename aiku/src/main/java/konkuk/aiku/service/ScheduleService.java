package konkuk.aiku.service;

import konkuk.aiku.controller.dto.ScheduleCond;
import konkuk.aiku.domain.*;
import konkuk.aiku.event.ScheduleEventPublisher;
import konkuk.aiku.event.UserPointEventPublisher;
import konkuk.aiku.exception.AlreadyInException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static konkuk.aiku.service.dto.ServiceDtoUtils.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupsRepository groupsRepository;
    private final UsersRepository usersRepository;

    private final ScheduleEventPublisher scheduleEventPublisher;
    private final UserPointEventPublisher userPointEventPublisher;

    @Transactional
    public Long addSchedule(Users user, Long groupId, ScheduleServiceDto scheduleServiceDTO){
        Groups group = findGroupById(groupId);
        checkUserInGroup(user, group);

        Schedule schedule = Schedule.builder()
                .scheduleName(scheduleServiceDTO.getScheduleName())
                .location(createLocation(scheduleServiceDTO.getLocation()))
                .scheduleTime(scheduleServiceDTO.getScheduleTime())
                .status(ScheduleStatus.WAIT)
                .build();

        group.addSchedule(schedule);
        schedule.addUser(user, new UserSchedule());

        scheduleRepository.save(schedule);

        scheduleEventPublisher.scheduleAddEvent(schedule.getId(), schedule.getScheduleTime());
        userPointEventPublisher.userPointChangeEvent(user, 100, PointType.REWARD, PointChangeType.PLUS, schedule.getCreatedAt()); //스케줄 등록 시 보상으로 100아쿠 적립
        return schedule.getId();
    }

    @Transactional
    public Long modifySchedule(Users user, Long groupId, Long scheduleId, ScheduleServiceDto scheduleServiceDTO) {
        Long userId = user.getId();
        UserSchedule userSchedule = checkUserInSchedule(userId, scheduleId);

        Schedule schedule = userSchedule.getSchedule();

        Location location = createLocation(scheduleServiceDTO.getLocation());
        schedule.updateSchedule(scheduleServiceDTO.getScheduleName(), location, scheduleServiceDTO.getScheduleTime());

        return schedule.getId();
    }

    @Transactional
    public Long deleteSchedule(Users user, Long groupId, Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);

        checkUserInSchedule(user.getId(), scheduleId);
        scheduleRepository.deleteById(scheduleId);

        if(schedule.getStatus() == ScheduleStatus.WAIT){
            scheduleEventPublisher.scheduleDeleteEvent(scheduleId);
//            userPointEventPublisher.userPointChangeEvent(); //여러명도 가능하게끔
        }
        return scheduleId;
    }

    public ScheduleDetailServiceDto findScheduleDetail(Users user, Long groupId, Long scheduleId){
        Groups group = findGroupWithUser(groupId);

        checkUserInGroup(user, group);
        checkScheduleInGroup(groupId, scheduleId);

        Schedule schedule = findScheduleWithUser(scheduleId);

        List<Users> waitUsers = getWaitUsers(group, schedule);
        return ScheduleDetailServiceDto.toDto(schedule, waitUsers);
    }

    public GroupScheduleListServiceDto findGroupScheduleList(Users user, Long groupId, ScheduleCond cond){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        List<Schedule> schedules = scheduleRepository.findScheduleWithUserByGroupId(groupId, cond.getStartDate(), cond.getEndDate(), cond.getStatus());

        int[] scheduleStatus = new int[3]; //RUN, WAIT, TERM순서
        List<ScheduleSimpleServiceDto> dataDto = createScheduleDto(user, schedules, scheduleStatus);
        return GroupScheduleListServiceDto.toDto(group, scheduleStatus[0], scheduleStatus[1], scheduleStatus[2], dataDto);
    }

    public UserScheduleListServiceDto findUserScheduleList(Users user, ScheduleCond cond){
        List<Schedule> schedules = scheduleRepository.findUserScheduleByUserId(user.getId(), cond.getStartDate(), cond.getEndDate(), cond.getStatus())
                .stream().map(UserSchedule::getSchedule).toList();

        int[] scheduleStatus = new int[3]; //RUN, WAIT, TERM순서
        List<ScheduleSimpleServiceDto> dataDto = createScheduleDto(schedules, scheduleStatus);
        return UserScheduleListServiceDto.toDto(user, scheduleStatus[0], scheduleStatus[1], scheduleStatus[2], dataDto);
    }

    @Transactional
    public Long enterSchedule(Users user, Long groupId, Long scheduleId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);
        Schedule schedule = findScheduleById(scheduleId);
        checkUserAlreadyInSchedule(user.getId(), scheduleId);

        schedule.addUser(user, new UserSchedule());
        scheduleRepository.upScheduleUserCount(scheduleId);
        return schedule.getId();
    }

    @Transactional
    public Long exitSchedule(Users user, Long groupId, Long scheduleId) {
        Long userId = user.getId();

        UserSchedule userSchedule = checkUserInSchedule(userId, scheduleId);

        Schedule schedule = userSchedule.getSchedule();
        schedule.deleteUser(user, userSchedule);
        scheduleRepository.downScheduleUserCount(scheduleId);
        return schedule.getId();
    }

    public ScheduleResultServiceDto findScheduleResult(Users user, Long groupId, Long scheduleId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        Schedule schedule = findScheduleById(scheduleId);
        List<UserArrivalData> userArrivalDatas = schedule.getUserArrivalDatas();
        return ScheduleResultServiceDto.toDto(schedule, userArrivalDatas);
    }

    //==이벤트 서비스==

    @Transactional
    public void openScheduleMap(Long scheduleId){
        Schedule schedule = findScheduleById(scheduleId);
        schedule.setStatus(ScheduleStatus.RUN);
    }

    public Runnable publishScheduleMapOpenRunnable(Long scheduleId){
        return () -> {
            scheduleEventPublisher.scheduleOpenEvent(scheduleId);
        };
    }

    @Transactional
    public boolean createUserArrivalData(Long userId, Long scheduleId, LocalDateTime arriveTime){
        Users user = findUserById(userId);

        if(checkUserAlreadyArrive(userId, scheduleId)) return false;

        Schedule schedule = findScheduleById(scheduleId);
        schedule.addUserArrivalData(user, arriveTime);
        return true;
    }

    @Transactional
    public void createAllUserArrivalData(Long scheduleId){
        Schedule schedule = findScheduleWithUser(scheduleId);

        LocalDateTime autoCloseTime = schedule.getScheduleTime().plusMinutes(30);


        List<Users> autoLateUsers = getAutoLateUsers(schedule);

        autoLateUsers.stream().forEach((lateUser) -> schedule.addUserArrivalData(lateUser, autoCloseTime));
    }

    public boolean checkAllUserArrive(Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleWithArrivalData(scheduleId).orElse(null);
        if (schedule == null){
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }

        schedule.setStatus(ScheduleStatus.TERM);
        return schedule.getUserArrivalDatas().size() == schedule.getUserCount();
    }

    public void publishScheduleCloseEvent(Long scheduleId){
        scheduleEventPublisher.scheduleCloseEvent(scheduleId);
    }

    public Runnable publishScheduleCloseEventRunnable(Long scheduleId){
        return () -> scheduleEventPublisher.scheduleCloseEvent(scheduleId);
    }

    //==검증 메서드==
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

    private void checkScheduleInGroup(Long groupId, Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleByGroupIdAndScheduleId(groupId, scheduleId).orElse(null);
        if (schedule == null) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
    }

    private boolean checkUserAlreadyInSchedule(Long userId, Long scheduleId){
        try {
            checkUserInSchedule(userId, scheduleId);
            throw new AlreadyInException(ErrorCode.ALREADY_IN_SCHEDULE);
        }catch (NoAthorityToAccessException e){
            return true;
        }
    }

    private boolean checkUserAlreadyArrive(Long userId, Long scheduleId){
        UserArrivalData userArrivalData = scheduleRepository
                .findUserArrivalDataByUserIdAndScheduleId(userId, scheduleId).orElse(null);
        if (userArrivalData == null) {
            return false;
        }
        return true;
    }

    //==레파지토리 조회 메서드==
    private Groups findGroupById(Long groupId){
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }

    private Groups findGroupWithUser(Long groupId){
        Groups group = groupsRepository.findGroupWithUser(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }

    private Schedule findScheduleById(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if(schedule == null){
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }

    private Schedule findScheduleWithUser(Long scheduleId){
        Schedule schedule = scheduleRepository.findScheduleWithUser(scheduleId).orElse(null);
        if(schedule == null){
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }

    private Users findUserById(Long userId){
        Users user = usersRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_USER);
        }
        return user;
    }

    //==편의 메서드==
    // 그룹의 스케줄 (유저 참석/불참석 구분)
    private List<ScheduleSimpleServiceDto> createScheduleDto(Users user, List<Schedule> schedules, int[] scheduleStatus) {
        List<ScheduleSimpleServiceDto> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            ScheduleSimpleServiceDto dto = ScheduleSimpleServiceDto.toDto(schedule);

            boolean accept = schedule.getUsers().stream()
                    .map(UserSchedule::getUser)
                    .toList()
                    .contains(user);
            if(!accept) dto.setAccept(false);

            dtos.add(dto);

            if (schedule.getStatus() == ScheduleStatus.RUN) scheduleStatus[0]++;
            else if(schedule.getStatus() == ScheduleStatus.WAIT) scheduleStatus[1]++;
            else if(schedule.getStatus() == ScheduleStatus.TERM) scheduleStatus[2]++;
        }
        return dtos;
    }

    //유저가 참가중인 스케줄
    private List<ScheduleSimpleServiceDto> createScheduleDto(List<Schedule> schedules, int[] scheduleStatus) {
        List<ScheduleSimpleServiceDto> dtos = new ArrayList<>();
        for (Schedule schedule : schedules) {
            dtos.add(ScheduleSimpleServiceDto.toDto(schedule));

            if (schedule.getStatus() == ScheduleStatus.RUN) scheduleStatus[0]++;
            else if(schedule.getStatus() == ScheduleStatus.WAIT) scheduleStatus[1]++;
            else if(schedule.getStatus() == ScheduleStatus.TERM) scheduleStatus[2]++;
        }
        return dtos;
    }

    private List<Users> getWaitUsers(Groups group, Schedule schedule) {
        List<Users> acceptUsers = schedule.getUsers().stream()
                .map(UserSchedule::getUser)
                .toList();

        return group.getUserGroups().stream()
                .map(UserGroup::getUser)
                .filter((user) -> !acceptUsers.contains(user))
                .toList();
    }

    private List<Users> getAutoLateUsers(Schedule schedule){
        List<Users> scheduleUsers = schedule
                .getUsers().stream()
                .map(UserSchedule::getUser)
                .toList();

        List<Users> arrivalUsers = scheduleRepository.findUserArrivalDatasWithUserByScheduleId(schedule.getId()).stream()
                .map(UserArrivalData::getUser)
                .toList();

        return scheduleUsers.stream()
                .filter((scheduleUser) -> !arrivalUsers.contains(scheduleUser))
                .toList();
    }
}
