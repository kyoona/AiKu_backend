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

        GroupServiceDTO groupServiceDTO = new GroupServiceDTO();
        groupServiceDTO.setId(group.getId());
        groupServiceDTO.setGroupName(group.getGroupName());
        groupServiceDTO.setGroupImg(group.getGroupImg());
        groupServiceDTO.setDescription(group.getDescription());

        return groupServiceDTO;
    }

    public GroupDetailServiceDTO findGroupDetailById(Long userId, Long groupId) {
        checkNotUserInGroup(userId, groupId);

        GroupDetailServiceDTO groupDetailServiceDTO = new GroupDetailServiceDTO();

        Groups groups = groupsRepository.findById(groupId).get();
        groupDetailServiceDTO.setGroupId(groups.getId());
        groupDetailServiceDTO.setGroupName(groups.getGroupName());
        groupDetailServiceDTO.setGroupImg(groups.getGroupImg());
        groupDetailServiceDTO.setDescription(groups.getDescription());

        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);

        for (UserGroup userGroup : userGroups) {
            UserSimpleServiceDTO userSimpleServiceDTO = createUserSimpleServiceDTO(userGroup.getUser());
            groupDetailServiceDTO.getUsers().add(userSimpleServiceDTO);
        }
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
        UserSimpleServiceDTO userSimpleServiceDTO = new UserSimpleServiceDTO();
        userSimpleServiceDTO.setUserId(user.getId());
        userSimpleServiceDTO.setUsername(user.getUsername());
        userSimpleServiceDTO.setPhoneNumber(user.getPhoneNumber());
        userSimpleServiceDTO.setUserImg(user.getUserImg());
        userSimpleServiceDTO.setSetting(createSettingServiceDTO(user));
        userSimpleServiceDTO.setPoint(user.getPoint());
        userSimpleServiceDTO.setRole(user.getRole());
        return userSimpleServiceDTO;
    }

    private SettingServiceDTO createSettingServiceDTO(Users user){
        SettingServiceDTO settingServiceDTO = new SettingServiceDTO();
        Setting setting = user.getSetting();
        settingServiceDTO.setIsBettingAlarmOn(setting.isBettingAlarmOn());
        settingServiceDTO.setIsPinchAlarmOn(setting.isPinchAlarmOn());
        settingServiceDTO.setIsLocationInformationOn(setting.isLocationInformationOn());
        settingServiceDTO.setIsScheduleAlarmOn(setting.isScheduleAlarmOn());
        settingServiceDTO.setIsVoiceAuthorityOn(setting.isVoiceAuthorityOn());
        return settingServiceDTO;
    }
}
