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

    @Transactional
    public Long deleteGroup(Users user, Long groupId) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        groupsRepository.deleteById(groupId);
        return groupId;
    }

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
        return groupId;
    }

    public AnalyticsLateRatingServiceDto getLateAnalytics(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        List<UserArrivalData> userArrivalDatas = scheduleRepository.findUserArrivalDatasWithUserByGroupId(groupId);

        List<AnalyticsLateServiceDto> lateDto = new ArrayList<>();
        userArrivalDatas.stream()
                .collect(Collectors.groupingBy(data -> data.getUser().getId()))
                .forEach((userId, arrivalList) -> {
                    Integer totalLateTime = arrivalList.stream().collect(Collectors.summingInt(UserArrivalData::getTimeDifference));
                    if (arrivalList.size() != 0){
                        Users arrivalUser = arrivalList.get(0).getUser();
                        lateDto.add(AnalyticsLateServiceDto.createDto(arrivalUser, totalLateTime));
                    }
                });

        return new AnalyticsLateRatingServiceDto(lateDto);
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

    //==레파지토리 조회 메서드==
    private Groups findGroupById(Long groupId){
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }
}
