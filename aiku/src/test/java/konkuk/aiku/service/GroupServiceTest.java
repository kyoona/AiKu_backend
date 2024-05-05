package konkuk.aiku.service;

import konkuk.aiku.domain.Setting;
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
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
                .build();
        Long userId1 = usersRepository.save(user)
                .getId();

        //when
        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

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
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
                .build();
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        //when
        GroupServiceDTO modifyGroupServiceDTO = GroupServiceDTO.builder()
                .groupName("group modify")
                .groupImg("url2")
                .description("group1을 수정하였습니다.")
                .build();
        groupService.modifyGroup(userKaKaoId1, groupId, modifyGroupServiceDTO);

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
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
                .build();
        usersRepository.save(user);

        String userKaKaoId2 = "kakao2";
        Users user2 = Users.builder()
                .username("user2")
                .kakaoId(userKaKaoId2)
                .build();
        Long userId2 = usersRepository.save(user2)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        //when
        GroupServiceDTO modifyGroupServiceDTO = GroupServiceDTO.builder()
                .groupName("group modify")
                .groupImg("url2")
                .description("group1을 수정하였습니다.")
                .build();
        assertThatThrownBy(()-> groupService.modifyGroup(userKaKaoId2, groupId, modifyGroupServiceDTO))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup(){
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
                .build();
        usersRepository.save(user);

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        //when
        groupService.deleteGroup(userKaKaoId1, groupId);

        //then
        assertThat(groupService.findGroupById(groupId)).isNull();
    }

    @Test
    @DisplayName("그룹 조회")
    void findGroupDetailById(){
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId1)
                .build();
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        //when
        GroupDetailServiceDTO groupDetailServiceDTO = groupService.findGroupDetailById(userKaKaoId1, groupId);

        //then
        UserSimpleServiceDTO findUserDTO = groupDetailServiceDTO.getUsers().get(0);
        assertThat(findUserDTO.getUserKaKaoId()).isEqualTo(user.getKakaoId());
        assertThat(findUserDTO.getUsername()).isEqualTo(user.getUsername());

        assertThat(groupDetailServiceDTO.getGroupId()).isEqualTo(groupId);
        assertThat(groupDetailServiceDTO.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(groupDetailServiceDTO.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 조회-그룹에 속해 있지 않은 유저")
    void findGroupDetailByIdInFaultCondition(){
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId1)
                .build();
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        String userKaKaoId2 = "kakao2";
        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId2)
                .build();
        Long userId2 = usersRepository.save(user2)
                .getId();

        //when
        assertThatThrownBy(() -> groupService.findGroupDetailById(userKaKaoId2, groupId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 참가")
    void enterGroup(){
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId1)
                .build();
        usersRepository.save(user);

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        String userKaKaoId2 = "kakao2";
        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId2)
                .build();
        Long userId2 = usersRepository.save(user2)
                .getId();

        //when
        groupService.enterGroup(userKaKaoId2, groupId);

        //then
        assertThat(userGroupRepository.existsByUserIdAndGroupId(userId2, groupId)).isTrue();
    }

    @Test
    @DisplayName("그룹 퇴장")
    void exitGroup(){
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId1)
                .build();
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        //when
        groupService.exitGroup(userKaKaoId1, groupId);

        //then
        assertThat(userGroupRepository.existsByUserIdAndGroupId(userId1, groupId)).isFalse();
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 속해 있지 않은 유저")
    void exitGroupInFaultCondition(){
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .kakaoId(userKaKaoId1)
                .build();
        Long userId1 = usersRepository.save(user)
                .getId();

        GroupServiceDTO groupServiceDTO = GroupServiceDTO.builder()
                .groupName("group1")
                .groupImg("url1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userKaKaoId1, groupServiceDTO);

        String userKaKaoId2 = "kakao2";
        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user2);

        //when
        assertThatThrownBy(() -> groupService.exitGroup(userKaKaoId2, groupId)).isInstanceOf(RuntimeException.class);
    }
}