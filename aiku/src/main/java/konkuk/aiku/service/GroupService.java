package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.AlreadyInException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.BettingRepository;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.scheduler.SchedulerService;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupsRepository groupsRepository;
    private final ScheduleRepository scheduleRepository;
    private final BettingRepository bettingRepository;

    private final SchedulerService schedulerService;

    @Transactional
    public Long addGroup(Users user, GroupServiceDto groupDto){
        Groups group = Groups.createGroups(user, groupDto.getGroupName(), groupDto.getDescription());

        groupsRepository.save(group);
        return group.getId();
    }

    @Transactional
    public Long modifyGroup(Users user, Long groupId, GroupServiceDto groupDto) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        group.updateGroup(groupDto.getGroupName(), groupDto.getDescription());
        return group.getId();
    }

/*    @Transactional
    public Long deleteGroup(Users user, Long groupId) {
        Groups group = findGroupWithSchedule(groupId);

        checkUserInGroup(user, group);

        groupsRepository.deleteById(groupId);
        return groupId;
    }*/

    @Transactional
    public Long enterGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        checkUserAlreadyInGroup(user, group);

        group.addUser(user);
        groupsRepository.upGroupUserCount(groupId);
        return groupId;
    }

    @Transactional
    public Long exitGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        UserGroup userGroup = checkUserInGroup(user, group);

        group.deleteUser(userGroup);
        groupsRepository.downGroupUserCount(groupId);

        int userCount = group.getUserCount();
        if(userCount == 1){ //해당 유저
            deleteGroup(groupId);
        }
        return groupId;
    }

    public void deleteGroup(Long groupId){
        Groups group = findGroupWithSchedule(groupId);

        List<Long> scheduleIdList = group.getSchedules().stream()
                .map(Schedule::getId)
                .toList();

        groupsRepository.delete(group);

        scheduleIdList.forEach((scheduleId) -> schedulerService.deleteSchedule(scheduleId));
    }

    //==뷰 조회 메서드==
    public GroupDetailServiceDto findGroupDetail(Users user, Long groupId) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        List<UserGroup> userGroups = groupsRepository.findUserGroupWithUser(groupId);

        List<UserSimpleServiceDto> userSimpleServiceDtos = UserSimpleServiceDto.toDtoListByUserGroup(userGroups);
        GroupDetailServiceDto serviceDto = GroupDetailServiceDto.toDto(group, userSimpleServiceDtos);
        return serviceDto;
    }

    public GroupListServiceDto findGroupList(Users user){
        List<UserGroup> userGroups = groupsRepository.findUserGroupWithGroup(user.getId());

        List<GroupSimpleServiceDto> groupDtos = createGroupListDataDtos(userGroups);
        return GroupListServiceDto.toDto(user.getId(), groupDtos);
    }

    private List<GroupSimpleServiceDto> createGroupListDataDtos(List<UserGroup> userGroups) {
        List<GroupSimpleServiceDto> dto = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            Groups group = userGroup.getGroup();
            LocalDateTime groupLatestScheduleTime = group.getSchedules().stream()
                    .collect(Collectors.maxBy((s1, s2) -> s1.getScheduleTime().compareTo(s2.getScheduleTime())))
                    .map(Schedule::getScheduleTime)
                    .orElse(null);
            dto.add(GroupSimpleServiceDto.toDto(group, groupLatestScheduleTime));
        }
        return dto;
    }

    //TODO 수정s
    public AnalyticsLateRatingServiceDto getLateAnalytics(Users user, Long groupId){
/*        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        List<UserArrivalData> userArrivalDatas = scheduleRepository.findUserArrivalDatasWithUserByGroupId(groupId);

        List<AnalyticsLateServiceDto> lateDto = new ArrayList<>();
        Map<Users, Integer> userLateTimeMap = new LinkedHashMap<>();
        userArrivalDatas.stream()
                .collect(Collectors.groupingBy(data -> data.getUser()))
                .forEach((arrivalUser, arrivalList) -> {
                    Integer totalLateTime = (arrivalList == null) ? 0 :arrivalList.stream().collect(Collectors.summingInt(UserArrivalData::getTimeDifference));
                    userLateTimeMap.put(arrivalUser, totalLateTime);
                    if (arrivalList.size() != 0){
                        Users arrivalUser = arrivalList.get(0).getUser();
                        lateDto.add(AnalyticsLateServiceDto.createDto(arrivalUser, totalLateTime));
                    }
                });
        userLateTimeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect()


        return new AnalyticsLateRatingServiceDto(lateDto);*/
        return null;
    }

    public List<AnalyticsBettingServiceDto> getBettingAnalytics(Users user, Long groupId) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        List<AnalyticsBettingServiceDto> analyticsResult = new ArrayList<>();

        List<UserGroup> userGroups = group.getUserGroups();

        for (UserGroup userGroup : userGroups) {
            Users userInGroup = userGroup.getUser();
            Long userId = userInGroup.getId();

            int winCount = 0;
            int totalBettingCount = 0;

            List<Betting> bettorBettings = bettingRepository.findAllByBettorIdAndScheduleGroupId(userId, groupId);
            List<Betting> targetBettings = bettingRepository.findAllByTargetUserIdAndScheduleGroupId(userId, groupId);

            for (Betting bettorBetting : bettorBettings) {
                if (bettorBetting.getResultType().equals(ResultType.WIN)) {
                    winCount += 1;
                }
            }
            totalBettingCount += bettorBettings.size(); // 자신이 건 베팅/레이싱은 모두 포함

            for (Betting targetBetting : targetBettings) {
                if (targetBetting.getBettingType().equals(BettingType.RACING)) {
                    totalBettingCount += 1; // 타겟일 때는 레이싱만 포함

                    if (targetBetting.getResultType().equals(ResultType.LOSE)) {
                        winCount += 1;
                    }
                }
            }

            int winningRate = winCount / totalBettingCount * 100; // 백분율

            analyticsResult.add(
                    AnalyticsBettingServiceDto.createDto(user, winningRate)
            );
        }
        analyticsResult.sort(
                Comparator.comparingInt(AnalyticsBettingServiceDto::getWinningRate).reversed()
        );

        return analyticsResult;
    }

    //==검증 메서드==
    private UserGroup checkUserInGroup(Users user, Groups groups){
        UserGroup userGroup = groupsRepository.findByUserAndGroup(user, groups).orElse(null);
        if(userGroup == null){
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userGroup;
    }

    private UserGroup checkUserAlreadyInGroup(Users user, Groups groups){
        UserGroup userGroup = groupsRepository.findByUserAndGroup(user, groups).orElse(null);
        if(userGroup != null){
            throw new AlreadyInException(ErrorCode.ALREADY_IN_GROUP);
        }
        return userGroup;
    }

    //==엔티티 조회 메서드==
    private Groups findGroupById(Long groupId){
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }

    private Groups findGroupWithSchedule(Long groupId){
        Groups group = groupsRepository.findGroupWithSchedule(groupId);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }
}
