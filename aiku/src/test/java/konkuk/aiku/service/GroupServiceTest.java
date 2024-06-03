package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.service.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    GroupsRepository groupsRepository;
    @Mock
    ScheduleRepository scheduleRepository;

    @InjectMocks
    GroupService groupService;

    @Test
    @DisplayName("그룹 등록")
    void addGroup() {
        //given
        Users user = createUser(1L, "user1");
        GroupServiceDto dto = createDto(2L, "group2", "addGroup");
        Groups group = Groups.createGroups(user, dto.getGroupName(), dto.getDescription());

        when(groupsRepository.save(any(Groups.class))).thenReturn(group);

        //when
        Long groupId = groupService.addGroup(user, dto);

        //then
        assertThat(groupId).isEqualTo(group.getId());
    }

    @Test
    @DisplayName("그룹 수정")
    void modifyGroup() {
        //given
        Users user = createUser(3L, "user3");
        Groups group = Groups.createGroups(user, "group3", "수정 전 그룹3");
        UserGroup userGroup = group.getUserGroups().get(0);
        GroupServiceDto dto = createDto(4L, "group4", "modifyGroup");

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.of(userGroup));

        //when
        Long groupId = groupService.modifyGroup(user, group.getId(), dto);

        //then
        assertThat(groupId).isEqualTo(group.getId());
    }

    @Test
    @DisplayName("그룹 수정-존재하지 않는 그룹")
    void modifyFaultGroup() {
        //given
        Users user = createUser(3L, "user3");
        Groups group = Groups.createGroups(user, "group3", "수정 전 그룹3");
        GroupServiceDto dto = createDto(4L, "group4", "modifyGroup");

        when(groupsRepository.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(()->groupService.modifyGroup(user, group.getId(), dto)).isInstanceOf(NoSuchEntityException.class);
    }

/*    @Test
    @DisplayName("그룹 삭제")
    void deleteGroup() {
        //given
        Users user = createUser(5L, "user5");
        Groups group = Groups.createGroups(user, "group6", "그룹6");
        UserGroup userGroup = group.getUserGroups().get(0);

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.of(userGroup));
        doNothing().when(groupsRepository).deleteById(any());

        //when
        Long groupId = groupService.deleteGroup(user, group.getId());

        //then
        assertThat(groupId).isEqualTo(group.getId());
    }*/

    @Test
    @DisplayName("그룹 상세 조회")
    void findGroupDetail() {
        //given
        Users user = createUser(1L, "user1!");
        Groups group = Groups.createGroups(user, "group1", "그룹1");
        UserGroup userGroup = group.getUserGroups().get(0);

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.of(userGroup));
        when(groupsRepository.findUserGroupWithUser(any())).thenReturn(List.of(userGroup));

        //when
        GroupDetailServiceDto groupResponse = groupService.findGroupDetail(user, group.getId());

        //then
        assertThat(groupResponse.getGroupName()).isEqualTo(group.getGroupName());
        assertThat(groupResponse.getDescription()).isEqualTo(group.getDescription());

        UserSimpleServiceDto userResponse = groupResponse.getUsers().get(0);
        assertThat(userResponse.getUserId()).isEqualTo(user.getId());
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("그룹 목록 조회")
    void findGroupList() {
        //given
        Users user = createUser(1L, "user1!");
        Groups group1 = Groups.createGroups(user, "group1", "그룹1");
        Groups group2 = Groups.createGroups(user, "group2", "그룹2");
        List<UserGroup> userGroups = List.of(group1.getUserGroups().get(0), group2.getUserGroups().get(0));

        when(groupsRepository.findUserGroupWithGroup(any(Long.class))).thenReturn(userGroups);

        //when
        GroupListServiceDto response = groupService.findGroupList(user);

        //then
        assertThat(response.getUserId()).isEqualTo(user.getId());

        List<GroupSimpleServiceDto> responseData = response.getData();
        assertThat(responseData.size()).isEqualTo(2);
        assertThat(responseData.stream().map(GroupSimpleServiceDto::getGroupName)).contains(group1.getGroupName(), group2.getGroupName());
    }

    @Test
    @DisplayName("그룹 입장")
    void enterGroup() {
        //given
        Users user = createUser(1L, "user1");
        Users enterUser = createUser(2L, "enterUser");
        Groups group = Groups.createGroups(user, "group1", "그룹1");

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));

        //when
        Long groupId = groupService.enterGroup(enterUser, group.getId());

        //then
        assertThat(groupId).isEqualTo(group.getId());
        assertThat(group.getUserGroups().size()).isEqualTo(2);
        assertThat(group.getUserGroups().stream().map((UserGroup::getUser))).contains(user, enterUser);
    }

/*    @Test
    @DisplayName("그룹 퇴장")
    void exitGroup() {
        //given
        Users user = createUser(1L, "user1");
        Groups group = Groups.createGroups(user, "group1", "그룹1");
        UserGroup userGroup = group.getUserGroups().get(0);

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.of(userGroup));

        //when
        Long groupId = groupService.exitGroup(user, group.getId());

        //then
        assertThat(groupId).isEqualTo(group.getId());
        assertThat(group.getUserGroups().size()).isEqualTo(0);
    }*/

    @Test
    @DisplayName("그룹 퇴장-유저가 그룹에 없을 때")
    void exitGroupWhenUserNotInGroup() {
        //given
        Users user = createUser(1L, "user1");
        Groups group = Groups.createGroups(user, "group1", "그룹1");

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(()->groupService.exitGroup(user, group.getId())).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("그룹 분석-누적 시간 조회")
    void getLateAnalytics(){
        //given
        Users user = createUser(1L, "user1");
        Groups group = Groups.createGroups(user, "group1", "그룹1");
        UserGroup userGroup = group.getUserGroups().get(0);

        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .scheduleName("스케줄1")
                .scheduleTime(LocalDateTime.of(2024, 5,1, 1, 10))
                .build();
        schedule1.addUserArrivalData(user, LocalDateTime.of(2024, 5,1, 1, 20));

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .scheduleName("스케줄2")
                .scheduleTime(LocalDateTime.of(2024, 5,1, 1, 10))
                .build();
        schedule2.addUserArrivalData(user, LocalDateTime.of(2024, 5,1, 1, 20));


        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.of(userGroup));
        when(scheduleRepository.findUserArrivalDatasWithUserByGroupId(any())).thenReturn(List.of(schedule1.getUserArrivalDatas().get(0), schedule2.getUserArrivalDatas().get(0)));

        //when
        AnalyticsLateRatingServiceDto serviceDto = groupService.getLateAnalytics(user, group.getId());

        //then
        assertThat(serviceDto.getData().stream().map(AnalyticsLateServiceDto::getTotalLateMinute)).contains(-20);
        assertThat(serviceDto.getData().stream().map((dto)-> dto.getUser().getUserId())).contains(user.getId());
    }

    Users createUser(Long id, String userName){
        return Users.builder()
                .id(id)
                .username(userName)
                .setting(new Setting(true, true, true, true, true))
                .build();
    }

    GroupServiceDto createDto(Long id, String groupName, String description){
        return GroupServiceDto.builder()
                .id(id)
                .groupName(groupName)
                .description(description)
                .build();
    }
}