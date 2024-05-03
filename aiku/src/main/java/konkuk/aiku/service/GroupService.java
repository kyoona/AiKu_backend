package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GroupService {

    private final GroupsRepository groupsRepository;
    private final UsersRepository usersRepository;
    private final UserGroupRepository userGroupRepository;

    public GroupService(GroupsRepository groupsRepository, UsersRepository usersRepository, UserGroupRepository userGroupRepository) {
        this.groupsRepository = groupsRepository;
        this.usersRepository = usersRepository;
        this.userGroupRepository = userGroupRepository;
    }

    @Transactional
    public Long addGroup(Long userId, GroupServiceDTO groupServiceDTO){
        Groups group = new Groups();
        group.setGroupName(groupServiceDTO.getGroupName());
        group.setGroupImg(groupServiceDTO.getGroupImg());
        group.setDescription(groupServiceDTO.getDescription());

        Long groupId = groupsRepository.save(group)
                .getId();

        Users user = usersRepository.findById(userId).get();

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        return groupId;
    }

    @Transactional
    public void modifyGroup(Long userId, Long groupId, GroupServiceDTO groupServiceDTO) {
        checkNotUserInGroup(userId, groupId);

        Groups group = groupsRepository.findById(groupId).get();
        if(StringUtils.hasText(groupServiceDTO.getGroupName())){
            group.setGroupName(groupServiceDTO.getGroupName());
        }
        if(StringUtils.hasText(groupServiceDTO.getDescription())){
            group.setDescription(groupServiceDTO.getDescription());
        }
        if(groupServiceDTO != null){
            group.setGroupImg(groupServiceDTO.getGroupImg());
        }
    }

    @Transactional
    public void deleteGroup(Long userId, Long groupId) {
        checkNotUserInGroup(userId, groupId);

        userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        groupsRepository.deleteById(groupId);
    }

    public GroupServiceDTO findGroupById(Long id){
        Groups group = groupsRepository.findById(id).orElse(null);
        if(group == null) return null;

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .groupImg(group.getGroupImg())
                .build();
        return groupServiceDTO;
    }

    public GroupDetailServiceDTO findGroupDetailById(Long userId, Long groupId) {
        checkNotUserInGroup(userId, groupId);

        Groups group = groupsRepository.findById(groupId).get();
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);

        List<UserSimpleServiceDTO> userSimpleServiceDTOS = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            userSimpleServiceDTOS.add(createUserSimpleServiceDTO(userGroup.getUser()));
        }

        GroupDetailServiceDTO groupDetailServiceDTO = GroupDetailServiceDTO.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .groupImg(group.getGroupImg())
                .description(group.getDescription())
                .users(userSimpleServiceDTOS)
                .build();
        return groupDetailServiceDTO;
    }

    private boolean checkNotUserInGroup(Long userId, Long groupId){
        boolean isIn = userGroupRepository.existsByUserIdAndGroupId(userId, groupId);
        if(!isIn){
            throw new RuntimeException("권한 없지롱"); //TODO
        }
        return !isIn;
    }

    private UserSimpleServiceDTO createUserSimpleServiceDTO(Users user){
        UserSimpleServiceDTO userSimpleServiceDTO = UserSimpleServiceDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .userImg(user.getUserImg())
                .setting(createSettingServiceDTO(user))
                .point(user.getPoint())
                .role(user.getRole())
                .build();
        return userSimpleServiceDTO;
    }

    private SettingServiceDTO createSettingServiceDTO(Users user){
        Setting setting = user.getSetting();
        SettingServiceDTO settingServiceDTO = SettingServiceDTO.builder()
                .isBettingAlarmOn(setting.isBettingAlarmOn())
                .isPinchAlarmOn(setting.isPinchAlarmOn())
                .isLocationInformationOn(setting.isLocationInformationOn())
                .isScheduleAlarmOn(setting.isScheduleAlarmOn())
                .isVoiceAuthorityOn(setting.isVoiceAuthorityOn())
                .build();
        return settingServiceDTO;
    }
}
