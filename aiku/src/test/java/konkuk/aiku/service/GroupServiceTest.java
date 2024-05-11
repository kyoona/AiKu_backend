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

    private Users userA;
    private Users userB;
    private Users userC;


    @BeforeEach
    void beforeEach(){
        userA = Users.builder()
                .username("userA")
                .phoneNumber("010-1111-1111")
                .userImg("http://userA.img")
                .point(1000)
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userA);

        userB = Users.builder()
                .username("userB")
                .phoneNumber("010-2222-2222")
                .userImg("http://userB.img")
                .point(2000)
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(userB);

        userC = Users.builder()
                .username("userC")
                .phoneNumber("010-3333-3333")
                .userImg("http://userC.img")
                .point(3000)
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userC);
    }

    @Test
    @Commit
    @DisplayName("그룹 등록")
    void addGroup() {
        //when
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);

        //then
        Groups findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(findGroup.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @Commit
    @DisplayName("그룹 수정")
    void modifyGroup() throws IllegalAccessException {
        //given

        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .description("group1을 수정하였습니다.")
                .build();
        groupService.modifyGroup(userA, groupId, modifyGroupServiceDto);

        //then
        Groups findGroup = groupService.findGroupById(groupId);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDto.getGroupName());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDto.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() {
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .description("group1을 수정하였습니다.")
                .build();
        assertThatThrownBy(()-> groupService.modifyGroup(userB, groupId, modifyGroupServiceDto))
                .isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup(){
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        groupService.deleteGroup(userA, groupId);

        //then
        assertThatThrownBy(() -> groupService.findGroupById(groupId)).isInstanceOf(NoSuchEntityException.class);
    }

    @Test
    @DisplayName("그룹 조회")
    void findGroupDetailById(){
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        GroupDetailServiceDto groupDetailServiceDTO = groupService.findGroupDetailById(userA, groupId);

        //then
        UserSimpleServiceDto findUserDTO = groupDetailServiceDTO.getUsers().get(0);
        assertThat(findUserDTO.getUserId()).isEqualTo(userA.getId());
        assertThat(findUserDTO.getUsername()).isEqualTo(userA.getUsername());
        assertThat(findUserDTO.getPoint()).isEqualTo(userA.getPoint());

        assertThat(groupDetailServiceDTO.getGroupId()).isEqualTo(groupId);
        assertThat(groupDetailServiceDTO.getGroupName()).isEqualTo(groupServiceDTO.getGroupName());
        assertThat(groupDetailServiceDTO.getDescription()).isEqualTo(groupServiceDTO.getDescription());
    }

    @Test
    @DisplayName("그룹 조회-그룹에 속해 있지 않은 유저")
    void findGroupDetailByIdInFaultCondition(){
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);

        //when
        assertThatThrownBy(() -> groupService.findGroupDetailById(userB, groupId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @Commit
    @DisplayName("그룹 참가")
    void enterGroup(){
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        groupService.enterGroup(userB, groupId);

        //then
        assertThat(userGroupRepository.findByUserIdAndGroupId(userB.getId(), groupId)).isNotEmpty();
    }

    @Test
    @Commit
    @DisplayName("그룹 퇴장")
    void exitGroup(){
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);
        em.flush();
        em.clear();

        //when
        groupService.exitGroup(userA, groupId);

        //then
        assertThat(userGroupRepository.findByUserIdAndGroupId(userA.getId(), groupId)).isEmpty();
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 속해 있지 않은 유저")
    void exitGroupInFaultCondition(){
        //given
        GroupServiceDto groupServiceDTO = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();
        Long groupId = groupService.addGroup(userA, groupServiceDTO);

        //when
        assertThatThrownBy(() -> groupService.exitGroup(userB, groupId)).isInstanceOf(RuntimeException.class);
    }
}