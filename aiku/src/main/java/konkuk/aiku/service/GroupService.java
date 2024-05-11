package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupsRepository groupsRepository;

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

        group.modifyGroup(groupDto.getGroupName(), groupDto.getDescription());
        return group.getId();
    }

    @Transactional
    public Long deleteGroup(Users user, Long groupId) {
        Groups group = findGroupById(groupId);

        checkUserInGroup(user, group);

        groupsRepository.deleteById(groupId);
        return groupId;
    }

    public GroupDetailServiceDto findGroupDetailById(Users user, Long groupId) {
        Groups group = findGroupById(groupId);
        checkUserInGroup(user, group);

        List<UserGroup> userGroups = group.getUserGroups();

        List<UserSimpleServiceDto> userSimpleServiceDtos = UserSimpleServiceDto.toDtoListByUserGroup(userGroups);
        GroupDetailServiceDto serviceDto = GroupDetailServiceDto.toDto(group, userSimpleServiceDtos);
        return serviceDto;
    }

    @Transactional
    public Long enterGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);
        group.addUser(user);
        return groupId;
    }

    @Transactional
    public Long exitGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        UserGroup userGroup = checkUserInGroup(user, group);

        groupsRepository.deleteUserGroup(userGroup);
        return groupId;
    }

    private Groups findGroupById(Long groupId){
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }

    private UserGroup checkUserInGroup(Users user, Groups groups){
        UserGroup userGroup = groupsRepository.findByUserIdAndGroupId(user.getId(), groups.getId()).orElse(null);
        if(userGroup == null){
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userGroup;
    }
}
