package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.GroupDetailServiceDto;
import konkuk.aiku.service.dto.GroupListServiceDto;
import konkuk.aiku.service.dto.GroupServiceDto;
import konkuk.aiku.service.dto.GroupSimpleServiceDto;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class GroupServiceIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(GroupServiceIntegrationTest.class);
    @Autowired
    private EntityManager em;
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UsersRepository usersRepository;

    private Users userA;
    private Users userB;
    private Users userC;

    GroupServiceDto groupServiceDTO1;
    GroupServiceDto groupServiceDTO2;

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

        groupServiceDTO1 = GroupServiceDto.builder()
                .groupName("group1")
                .description("group1입니다.")
                .build();

        groupServiceDTO2 = GroupServiceDto.builder()
                .groupName("group2")
                .description("group2입니다.")
                .build();
    }

    @AfterEach
    void afterEach(){
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("그룹 등록")
    void addGroup() {
        //when
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(findGroup).isNotNull();
        assertThat(findGroup.getGroupName()).isEqualTo(groupServiceDTO1.getGroupName());
        assertThat(findGroup.getDescription()).isEqualTo(groupServiceDTO1.getDescription());

        assertThat( groupsRepository.findByUserAndGroup(userA, findGroup)).isNotEmpty();
    }

    @Test
    @DisplayName("그룹 수정")
    void modifyGroup() {
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .description("group1을 수정하였습니다.")
                .build();
        groupService.modifyGroup(userA, groupId, modifyGroupServiceDto);

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(findGroup.getGroupName()).isEqualTo(modifyGroupServiceDto.getGroupName());
        assertThat(findGroup.getDescription()).isEqualTo(modifyGroupServiceDto.getDescription());
    }

    @Test
    @DisplayName("그룹 수정-그룹에 속해 있지 않은 유저")
    void modifyGroupInFaultCondition() {
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //when
        GroupServiceDto modifyGroupServiceDto = GroupServiceDto.builder()
                .groupName("group modify")
                .description("group1을 수정하였습니다.")
                .build();
        assertThatThrownBy(()-> groupService.modifyGroup(userB, groupId, modifyGroupServiceDto))
                .isInstanceOf(NoAthorityToAccessException.class);
    }

/*    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup(){
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //when
        groupService.deleteGroup(userA, groupId);

        //then
        assertThat(groupsRepository.findById(groupId)).isEmpty();
    }*/

    @Test
    @DisplayName("그룹 상세 조회")
    void findGroupDetail(){
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);
        groupService.enterGroup(userB, groupId);

        //when
        GroupDetailServiceDto groupDetailServiceDTO = groupService.findGroupDetail(userA, groupId);

        //then
        assertThat(groupDetailServiceDTO.getUsers().size()).isEqualTo(2);
        assertThat(groupDetailServiceDTO.getUsers()).filteredOn( user ->
            (user.getUserId() == userA.getId()) || (user.getUserId() == userB.getId())
        ).size().isEqualTo(2);

        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(groupDetailServiceDTO.getGroupId()).isEqualTo(findGroup.getId());
        assertThat(groupDetailServiceDTO.getGroupName()).isEqualTo(findGroup.getGroupName());
        assertThat(groupDetailServiceDTO.getDescription()).isEqualTo(findGroup.getDescription());
    }

    @Test
    @DisplayName("그룹 상세 조회-그룹에 속해 있지 않은 유저")
    void findGroupDetailInFaultCondition(){
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //when
        assertThatThrownBy(() -> groupService.findGroupDetail(userB, groupId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("그룹 목록 조회")
    void findGroupList(){
        //given
        Long groupId1 = groupService.addGroup(userA, groupServiceDTO1);
        Long groupId2 = groupService.addGroup(userA, groupServiceDTO2);

        //when
        GroupListServiceDto serviceDto = groupService.findGroupList(userA);

        //then
        assertThat(serviceDto.getUserId()).isEqualTo(userA.getId());

        List<GroupSimpleServiceDto> dataDto = serviceDto.getData();
        assertThat(dataDto.size()).isEqualTo(2);
        assertThat(dataDto.stream().map(GroupSimpleServiceDto::getGroupId).toList()).contains(groupId1, groupId2);
    }

    @Test
    @DisplayName("그룹 참가")
    void enterGroup(){
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //when
        groupService.enterGroup(userB, groupId);

        //then
        Groups findGroup = groupsRepository.findById(groupId).orElse(null);
        assertThat(groupsRepository.findByUserAndGroup(userA, findGroup)).isNotEmpty();
    }

    @Test
    @DisplayName("그룹 퇴장-그룹에 속해 있지 않은 유저")
    void exitGroupInFaultCondition(){
        //given
        Long groupId = groupService.addGroup(userA, groupServiceDTO1);

        //when
        assertThatThrownBy(() -> groupService.exitGroup(userB, groupId)).isInstanceOf(RuntimeException.class);
    }
}