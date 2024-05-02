package konkuk.aiku.service;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.GroupServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

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
        if(isNotUserInGroup(userId, groupId)){
            throw new RuntimeException("권한 없지롱"); //TODO
        }

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
        if(isNotUserInGroup(userId, groupId)){
            throw new RuntimeException("권한 없지롱"); //TODO
        }

        userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        groupsRepository.deleteById(groupId);
    }

    public Groups findGroupById(Long id){
        Optional<Groups> group = groupsRepository.findById(id);
        return group.orElse(null);
    }

    private boolean isNotUserInGroup(Long userId, Long groupId){
        return !userGroupRepository.existsByUserIdAndGroupId(userId, groupId);
    }
}
