package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GroupService {

    private final GroupsRepository groupsRepository;
    private final UsersRepository usersRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public Long addGroup(Users user, GroupServiceDto groupServiceDTO){
        Groups group = Groups.builder()
                .groupName(groupServiceDTO.getGroupName())
                .description(groupServiceDTO.getDescription())
                .build();

        groupsRepository.save(group);

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .build();
        userGroupRepository.save(userGroup);

        return group.getId();
    }

    @Transactional
    public Long modifyGroup(Users user, Long groupId, GroupServiceDto groupServiceDTO) {
        Groups group = checkUserInGroup(user.getId(), groupId).getGroup();
        group.setGroupName(groupServiceDTO.getGroupName());
        group.setDescription(groupServiceDTO.getDescription());
        return group.getId();
    }

    @Transactional
    public Long deleteGroup(Users user, Long groupId) {
        Long userId = user.getId();
        checkUserInGroup(userId, groupId);

        userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        groupsRepository.deleteById(groupId);
        return groupId;
    }

    public GroupDetailServiceDto findGroupDetailById(Users user, Long groupId) {
        Long userId = user.getId();
        Groups group = checkUserInGroup(userId, groupId).getGroup();

        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);

        List<UserSimpleServiceDto> userSimpleServiceDtos = UserSimpleServiceDto.toDtoListByUserGroup(userGroups);
        GroupDetailServiceDto serviceDto = GroupDetailServiceDto.toDto(group, userSimpleServiceDtos);
        return serviceDto;
    }

    @Transactional
    public Long enterGroup(Users user, Long groupId){
        Groups group = findGroupById(groupId);

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .build();
        userGroupRepository.save(userGroup);
        return groupId;
    }

    @Transactional
    public Long exitGroup(Users user, Long groupId){
        Long userId = user.getId();
        checkUserInGroup(userId, groupId);
        userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        return groupId;
    }

    public Groups findGroupById(Long groupId){
        Groups group = groupsRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_GROUP);
        }
        return group;
    }

    private UserGroup checkUserInGroup(Long userId, Long groupId){
        Optional<UserGroup> userGroup = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if(userGroup.isEmpty()){
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userGroup.get();
    }
}
