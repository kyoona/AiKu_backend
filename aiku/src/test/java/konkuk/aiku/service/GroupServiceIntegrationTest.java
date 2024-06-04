package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@Slf4j
class GroupServiceIntegrationTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private GroupService groupService;

    private Users userA1;
    private Users userA2;
    private Users userB;

    GroupServiceDto groupServiceDtoA;
    GroupServiceDto groupServiceDtoB;

    @BeforeEach
    void beforeEach(){
        userA1 = Users.builder()
                .username("userA1")
                .phoneNumber("010-1111-1111")
                .userImg("http://userA1.img")
                .point(1000)
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userA1);

        userA2 = Users.builder()
                .username("userA2")
                .phoneNumber("010-2222-2222")
                .userImg("http://userA2.img")
                .point(2000)
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(userA2);

        userB = Users.builder()
                .username("userC")
                .phoneNumber("010-3333-3333")
                .userImg("http://userC.img")
                .point(3000)
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userB);

        groupServiceDtoA = GroupServiceDto.builder()
                .groupName("groupA")
                .description("groupA입니다.")
                .build();

        groupServiceDtoB = GroupServiceDto.builder()
                .groupName("groupB")
                .description("groupB입니다.")
                .build();

        log.info("@BeforeEach Completion");
    }

//    @AfterEach
    void afterEach(){
        scheduleRepository.deleteAll();
        groupsRepository.deleteAll();
        usersRepository.deleteAll();
        log.info("@AfterEach Completion");
    }

    @Test
    @DisplayName("그룹 등록")
    void addGroup() {
        //when
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(findGroup).isNotNull();
        assertThat(findGroup.getGroupName()).isEqualTo(groupServiceDtoA.getGroupName());
        assertThat(findGroup.getDescription()).isEqualTo(groupServiceDtoA.getDescription());

        UserGroup userGroup = groupsRepository.findByUserAndGroup(userA1, findGroup.getId()).orElse(null);
        Users findUserA1 = userGroup.getUser();
        assertThat(findUserA1.getId()).isEqualTo(userA1.getId());
        assertThat(findUserA1.getUsername()).isEqualTo(userA1.getUsername());
        assertThat(findUserA1.getPhoneNumber()).isEqualTo(userA1.getPhoneNumber());
        assertThat(findUserA1.getPoint()).isEqualTo(userA1.getPoint());
    }

    @Test
    @DisplayName("그룹 수정")
    void modifyGroup() {
        //given
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .description("group1을 수정하였습니다.")
                .build();
        groupService.modifyGroup(userA1, groupId, modifyGroupServiceDto);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDto.getGroupName());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDto.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() {
        //given
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .description("group1을 수정하였습니다.")
                .build();
        assertThatThrownBy(()-> groupService.modifyGroup(userB, groupId, modifyGroupServiceDto))
                .isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("그룹 참가")
    void enterGroup(){
        //given
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        groupService.enterGroup(userB, groupId);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(findGroup.getUserCount()).isEqualTo(2);

        Users findUser = groupsRepository.findByUserAndGroup(userB, findGroup.getId()).get().getUser();
        assertThat(findUser.getId()).isEqualTo(userB.getId());
        assertThat(findUser.getUsername()).isEqualTo(userB.getUsername());
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 멤버 남아 있음")
    public void exitGroup() {
        //given
        Long groupAId = groupService.addGroup(userA1, groupServiceDtoA);
        groupService.enterGroup(userA2, groupAId);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        groupService.exitGroup(userA1, groupAId);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        Groups findGroup = groupsRepository.findById(groupAId).orElse(null);
        assertThat(findGroup).isNotNull();
        assertThat(findGroup.getUserCount()).isEqualTo(1);

        UserGroup userGroup = groupsRepository.findByUserAndGroup(userA1, findGroup.getId()).orElse(null);
        assertThat(userGroup).isNull();
    }

    @Test
    @DisplayName("그룹 퇴장-멤버 없음으로 그룹 삭제")
    public void exitGroupAndGroupDelete() {
        //given
        Long groupAId = groupService.addGroup(userA1, groupServiceDtoA);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        groupService.exitGroup(userA1, groupAId);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        Groups findGroup = groupsRepository.findById(groupAId).orElse(null);
        assertThat(findGroup).isNull();

        UserGroup userGroup = groupsRepository.findByUserAndGroup(userA1, groupAId).orElse(null);
        assertThat(userGroup).isNull();
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 속해 있지 않은 유저")
    void exitGroupInFaultCondition(){
        //given
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);

        //when
        assertThatThrownBy(() -> groupService.exitGroup(userB, groupId)).isInstanceOf(RuntimeException.class);
    }


    //==뷰 조회==
    @Test
    @DisplayName("그룹 상세 조회")
    void findGroupDetail(){
        //given
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);
        groupService.enterGroup(userA2, groupId);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        GroupDetailServiceDto dto = groupService.findGroupDetail(userA1, groupId);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(dto.getGroupId()).isEqualTo(findGroup.getId());
        assertThat(dto.getGroupName()).isEqualTo(findGroup.getGroupName());
        assertThat(dto.getDescription()).isEqualTo(findGroup.getDescription());
        assertThat(dto.getUserCount()).isEqualTo(findGroup.getUserCount());

        List<UserSimpleServiceDto> findUsers = dto.getUsers();
        assertThat(findUsers.size()).isEqualTo(2);
        assertThat(findUsers.stream().map(UserSimpleServiceDto::getUserId)).contains(userA1.getId(), userA2.getId());
        assertThat(findUsers.stream().map(UserSimpleServiceDto::getUsername)).contains(userA1.getUsername(), userA2.getUsername());
        assertThat(findUsers.stream().map(UserSimpleServiceDto::getPhoneNumber)).contains(userA1.getPhoneNumber(), userA2.getPhoneNumber());
        assertThat(findUsers.stream().map(UserSimpleServiceDto::getPoint)).contains(userA1.getPoint(), userA2.getPoint());
    }

    @Test
    @DisplayName("그룹 상세 조회-그룹에 속해 있지 않은 유저")
    void findGroupDetailInFaultCondition(){
        //given
        Long groupId = groupService.addGroup(userA1, groupServiceDtoA);

        //when
        assertThatThrownBy(() -> groupService.findGroupDetail(userB, groupId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 목록 조회")
    void findGroupList(){
        //given
        Long groupIdA = groupService.addGroup(userA1, groupServiceDtoA);
        Long groupIdB = groupService.addGroup(userA1, groupServiceDtoB);

        groupService.enterGroup(userA2, groupIdA);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        GroupListServiceDto dto = groupService.findGroupList(userA1);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        assertThat(dto.getUserId()).isEqualTo(userA1.getId());

        List<GroupSimpleServiceDto> data = dto.getData();
        assertThat(data.size()).isEqualTo(2);
        assertThat(data.stream().map(GroupSimpleServiceDto::getGroupId).toList()).contains(groupIdA, groupIdB);
        assertThat(data.stream().map(GroupSimpleServiceDto::getGroupName).toList()).contains(groupServiceDtoA.getGroupName(), groupServiceDtoB.getGroupName());
        assertThat(data.stream().map(GroupSimpleServiceDto::getDescription).toList()).contains(groupServiceDtoA.getDescription(), groupServiceDtoB.getDescription());
        assertThat(data.stream().map(GroupSimpleServiceDto::getUserCount).toList()).contains(1, 2);
    }
}