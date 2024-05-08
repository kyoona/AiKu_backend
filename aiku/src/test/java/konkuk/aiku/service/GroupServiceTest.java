package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.GroupDetailServiceDto;
import konkuk.aiku.service.dto.GroupServiceDto;
import konkuk.aiku.service.dto.UserSimpleServiceDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class GroupServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Test
    @Commit
    @DisplayName("그룹 등록")
    void addGroup() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);
        em.flush();
        em.clear();

        //when
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);

        //then
        Groups findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(findGroup.getGroupImg()).isEqualTo(groupServiceDTO.getGroupImg());
        assertThat(findGroup.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @Commit
    @DisplayName("그룹 수정")
    void modifyGroup() throws IllegalAccessException {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .groupImg("url2")
                .description("group1을 수정하였습니다.")
                .build();
        groupService.modifyGroup(user, groupId, modifyGroupServiceDto);

        //then
        Groups findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDto.getGroupName());
        assertThat(findGroup.getGroupImg()).isEqualTo(modifyGroupServiceDto.getGroupImg());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDto.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .username("user2")
                .build();
        usersRepository.save(user2);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .groupImg("url2")
                .description("group1을 수정하였습니다.")
                .build();
        assertThatThrownBy(()-> groupService.modifyGroup(user2, groupId, modifyGroupServiceDto))
                .isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup(){
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        groupService.deleteGroup(user, groupId);

        //then
        assertThatThrownBy(() -> groupService.findGroupById(groupId)).isInstanceOf(NoSuchEntityException.class);
    }

    @Test
    @DisplayName("그룹 조회")
    void findGroupDetailById(){
        //given
        Users user = Users.builder()
                .username("user1")
                .point(1000)
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        GroupDetailServiceDto groupDetailServiceDTO = groupService.findGroupDetailById(user, groupId);

        //then
        UserSimpleServiceDto findUserDTO = groupDetailServiceDTO.getUsers().get(0);
        assertThat(findUserDTO.getUserId()).isEqualTo(user.getId());
        assertThat(findUserDTO.getUsername()).isEqualTo(user.getUsername());
        assertThat(findUserDTO.getPoint()).isEqualTo(user.getPoint());

        assertThat(groupDetailServiceDTO.getGroupId()).isEqualTo(groupId);
        assertThat(groupDetailServiceDTO.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(groupDetailServiceDTO.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 조회-그룹에 속해 있지 않은 유저")
    void findGroupDetailByIdInFaultCondition(){
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user2);

        //when
        assertThatThrownBy(() -> groupService.findGroupDetailById(user2, groupId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @Commit
    @DisplayName("그룹 참가")
    void enterGroup(){
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user2);
        em.flush();
        em.clear();

        //when
        groupService.enterGroup(user2, groupId);

        //then
        assertThat(userGroupRepository.findByUserIdAndGroupId(user2.getId(), groupId)).isNotEmpty();
    }

    @Test
    @Commit
    @DisplayName("그룹 퇴장")
    void exitGroup(){
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        groupService.exitGroup(user, groupId);

        //then
        assertThat(userGroupRepository.findByUserIdAndGroupId(user.getId(), groupId)).isEmpty();
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 속해 있지 않은 유저")
    void exitGroupInFaultCondition(){
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(user, groupServiceDTO);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user2);

        //when
        assertThatThrownBy(() -> groupService.exitGroup(user2, groupId)).isInstanceOf(RuntimeException.class);
    }
}