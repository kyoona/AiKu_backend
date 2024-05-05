package konkuk.aiku.service;

import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserRole;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.GroupDetailServiceDTO;
import konkuk.aiku.service.dto.GroupServiceDTO;
import konkuk.aiku.service.dto.UserSimpleServiceDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@Transactional
@SpringBootTest
class GroupServiceTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Test
    @DisplayName("그룹 등록")
    void addGroup() {
        //given
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        //when
        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        //then
        GroupServiceDTO findGroupDTO = groupService.findGroupById(groupId);
        assertThat(findGroupDTO.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(findGroupDTO.getGroupImg()).isEqualTo(groupServiceDTO.getGroupImg());
        assertThat(findGroupDTO.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 수정")
    void modifyGroup() throws IllegalAccessException {
        //given
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        //when
        GroupServiceDTO modifyGroupServiceDTO = GroupServiceDTO.builder()
                .groupName("group modify")
                .groupImg("url2")
                .description("group1을 수정하였습니다.")
                .build();
        groupService.modifyGroup(userId1, groupId, modifyGroupServiceDTO);

        //then
        GroupServiceDTO findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDTO.getGroupName());
        assertThat(findGroup.getGroupImg()).isEqualTo(modifyGroupServiceDTO.getGroupImg());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() throws IllegalAccessException {
        //given
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        Users user2 = new Users();
        user2.setUsername("user2");
        Long userId2 = usersRepository.save(user2)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        //when
        GroupServiceDTO modifyGroupServiceDTO = GroupServiceDTO.builder()
                .groupName("group modify")
                .groupImg("url2")
                .description("group1을 수정하였습니다.")
                .build();
        assertThatThrownBy(()-> groupService.modifyGroup(userId2, groupId, modifyGroupServiceDTO))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup(){
        //given
        Users user = new Users();
        user.setUsername("user1");
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        //when
        groupService.deleteGroup(userId1, groupId);

        //then
        assertThat(groupService.findGroupById(groupId)).isNull();
    }

    @Test
    @DisplayName("그룹 조회")
    void findGroupDetailById(){
        //given
        Users user = new Users();
        user.setUsername("user1");
        user.setRole(UserRole.USER);
        user.setSetting(new Setting(false, false, false, false, false));
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        //when
        GroupDetailServiceDTO groupDetailServiceDTO = groupService.findGroupDetailById(userId1, groupId);

        //then
        UserSimpleServiceDTO findUserDTO = groupDetailServiceDTO.getUsers().get(0);
        assertThat(findUserDTO.getUserId()).isEqualTo(user.getId());
        assertThat(findUserDTO.getUsername()).isEqualTo(user.getUsername());

        assertThat(groupDetailServiceDTO.getGroupId()).isEqualTo(groupId);
        assertThat(groupDetailServiceDTO.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(groupDetailServiceDTO.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 조회-그룹에 속해 있지 않은 유저")
    void findGroupDetailByIdInFaultCondition(){
        //given
        Users user = new Users();
        user.setUsername("user1");
        user.setRole(UserRole.USER);
        user.setSetting(new Setting(false, false, false, false, false));
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        Users user2 = new Users();
        user2.setUsername("user2");
        user2.setRole(UserRole.USER);
        user2.setSetting(new Setting(true, true, true, true, true));
        Long userId2 = usersRepository.save(user2)
                .getId();

        //when
        assertThatThrownBy(() -> groupService.findGroupDetailById(userId2, groupId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 참가")
    void enterGroup(){
        //given
        Users user = new Users();
        user.setUsername("user1");
        user.setRole(UserRole.USER);
        user.setSetting(new Setting(false, false, false, false, false));
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        Users user2 = new Users();
        user2.setUsername("user2");
        user2.setRole(UserRole.USER);
        user2.setSetting(new Setting(true, true, true, true, true));
        Long userId2 = usersRepository.save(user2)
                .getId();

        //when
        groupService.enterGroup(userId2, groupId);

        //then
        assertThat(userGroupRepository.existsByUserIdAndGroupId(userId2, groupId)).isTrue();
    }

    @Test
    @DisplayName("그룹 퇴장")
    void exitGroup(){
        //given
        Users user = new Users();
        user.setUsername("user1");
        user.setRole(UserRole.USER);
        user.setSetting(new Setting(false, false, false, false, false));
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        //when
        groupService.exitGroup(userId1, groupId);

        //then
        assertThat(userGroupRepository.existsByUserIdAndGroupId(userId1, groupId)).isFalse();
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 속해 있지 않은 유저")
    void exitGroupInFaultCondition(){
        //given
        Users user = new Users();
        user.setUsername("user1");
        user.setRole(UserRole.USER);
        user.setSetting(new Setting(false, false, false, false, false));
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userId1, groupServiceDTO);

        Users user2 = new Users();
        user2.setUsername("user2");
        user2.setRole(UserRole.USER);
        user2.setSetting(new Setting(true, true, true, true, true));
        Long userId2 = usersRepository.save(user2)
                .getId();

        //when
        assertThatThrownBy(() -> groupService.exitGroup(userId2, groupId)).isInstanceOf(RuntimeException.class);
    }
}