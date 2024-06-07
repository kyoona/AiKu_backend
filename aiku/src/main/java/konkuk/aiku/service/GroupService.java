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
import java.util.stream.Stream;

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

        checkUserInGroup(user, groupId);

        group.updateGroup(groupDto.getGroupName(), groupDto.getDescription());
        return group.getId();
    }

    @Transactional
    public Long enterGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        checkUserAlreadyInGroup(user, groupId);

        group.addUser(user);
        groupsRepository.upGroupUserCount(groupId);
        return groupId;
    }

    @Transactional
    public Long exitGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        UserGroup userGroup = groupsRepository.findByUserAndGroup(user, groupId).get();

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
        group.clearSchedule();
        groupsRepository.delete(group);

        scheduleIdList.forEach((scheduleId) -> schedulerService.deleteSchedule(scheduleId));
    }

    //==뷰 조회 메서드==
    public GroupDetailServiceDto findGroupDetail(Users user, Long groupId) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, groupId);

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

    public AnalyticsLateRatingServiceDto getLateAnalytics(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, groupId);

        List<UserArrivalData> userArrivalDatas = scheduleRepository.findUserArrivalDatasWithUserByGroupId(groupId);

        Map<Users, Integer> userLateTimeMap = new LinkedHashMap<>();
        userArrivalDatas.stream()
                .collect(Collectors.groupingBy(data -> data.getUser()))
                .forEach((arrivalUser, arrivalList) -> {
                    Integer totalLateTime = (arrivalList == null) ? 0 :arrivalList.stream().collect(Collectors.summingInt(UserArrivalData::getTimeDifference));
                    userLateTimeMap.put(arrivalUser, totalLateTime);
                });

        List<AnalyticsLateServiceDto> lateDto = new ArrayList<>();
        userLateTimeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ))
                .forEach((arrivalUser, totalLateTime) -> lateDto.add(AnalyticsLateServiceDto.createDto(arrivalUser, totalLateTime)));


        return new AnalyticsLateRatingServiceDto(lateDto);
    }

    public List<AnalyticsBettingServiceDto> getBettingAnalytics(Users user, Long groupId) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, groupId);

        List<AnalyticsBettingServiceDto> analyticsResult = new ArrayList<>();

        List<UserGroup> userGroups = group.getUserGroups();

        for (UserGroup userGroup : userGroups) {
            Users userInGroup = userGroup.getUser();
            Long userId = userInGroup.getId();

            int winCount = 0;
            int totalBettingCount = 0;

            List<Betting> bettorBettings = bettingRepository.findAllByBettorIdAndScheduleGroupId(userId, groupId);
            List<Betting> targetBettings = bettingRepository.findAllByTargetUserIdAndScheduleGroupId(userId, groupId);

            winCount += bettorBettings.stream()
                    .filter(betting -> betting.getBettingStatus().equals(BettingStatus.DONE))
                    .filter(betting -> betting.getResultType().equals(ResultType.WIN))
                    .count();

            totalBettingCount += bettorBettings.size(); // 자신이 건 베팅/레이싱은 모두 포함

            Stream<Betting> targetResultRacingStream = targetBettings.stream()
                    .filter(betting -> betting.getBettingStatus().equals(BettingStatus.DONE))
                    .filter(betting -> betting.getBettingType().equals(BettingType.RACING));

            totalBettingCount += targetResultRacingStream.count();
            winCount += targetResultRacingStream
                    .filter(betting -> betting.getBettingStatus().equals(BettingStatus.DONE))
                    .filter(betting -> betting.getResultType().equals(ResultType.LOSE))
                    .count();

            int winningRate = totalBettingCount == 0 ? 0 : winCount / totalBettingCount * 100; // 백분율

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
    private UserGroup checkUserInGroup(Users user, Long groupId){
        UserGroup userGroup = groupsRepository.findByUserAndGroup(user, groupId).orElse(null);
        if(userGroup == null){
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userGroup;
    }

    private UserGroup checkUserAlreadyInGroup(Users user, Long groupId){
        UserGroup userGroup = groupsRepository.findByUserAndGroup(user, groupId).orElse(null);
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
