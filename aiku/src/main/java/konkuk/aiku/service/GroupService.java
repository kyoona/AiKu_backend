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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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
    public Long addGroup(String kakaoId, GroupServiceDTO groupServiceDTO){
        Groups group = new Groups();
        group.setGroupName(groupServiceDTO.getGroupName());
        group.setGroupImg(groupServiceDTO.getGroupImg());
        group.setDescription(groupServiceDTO.getDescription());

        groupsRepository.save(group);

        Users user = findUserByKakaoId(kakaoId);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        return group.getId();
    }

    @Transactional
    public void modifyGroup(String kakaoId, Long groupId, GroupServiceDTO groupServiceDTO) {
        Long userId = findUserByKakaoId(kakaoId).getId();
        Groups group = checkUserInGroup(userId, groupId);

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
    public void deleteGroup(String kakaoId, Long groupId) {
        Long userId = findUserByKakaoId(kakaoId).getId();
        checkUserInGroup(userId, groupId);

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

    public GroupDetailServiceDTO findGroupDetailById(String kakaoId, Long groupId) {
        Long userId = findUserByKakaoId(kakaoId).getId();
        Groups group = checkUserInGroup(userId, groupId);

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

    @Transactional
    public void enterGroup(String kakaoId, Long groupId){
        Users user = findUserByKakaoId(kakaoId);
        Groups group = groupsRepository.findById(groupId).get();

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);
    }

    @Transactional
    public void exitGroup(String kakaoId, Long groupId){
        Long userId = findUserByKakaoId(kakaoId).getId();
        checkUserInGroup(userId, groupId);
        userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
    }

    private Groups checkUserInGroup(Long userId, Long groupId){
        Optional<Groups> group = userGroupRepository.findByUserIdAndGroupId(userId, groupId);
        if(group.isEmpty()){
            throw new RuntimeException("권한 없지롱"); //TODO
        }
        return group.get();
    }

    private Users findUserByKakaoId(String kakaoId){
        return usersRepository.findByKakaoId(kakaoId).get();
    }

    private UserSimpleServiceDTO createUserSimpleServiceDTO(Users user){
        UserSimpleServiceDTO userSimpleServiceDTO = UserSimpleServiceDTO.builder()
                .userKaKaoId(user.getKakaoId())
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
