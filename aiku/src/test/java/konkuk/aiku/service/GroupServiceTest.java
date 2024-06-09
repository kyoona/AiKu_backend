package konkuk.aiku.service;

import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.scheduler.SchedulerService;
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
    @Mock
    SchedulerService schedulerService;

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
        when(groupsRepository.findByUserAndGroup(any(Users.class), any())).thenReturn(Optional.of(userGroup));

        //when
        Long groupId = groupService.modifyGroup(user, group.getId(), dto);

        //then
        assertThat(groupId).isEqualTo(group.getId());
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

    @Test
    @DisplayName("그룹 퇴장")
    void exitGroup(){
        //given
        Users user1 = createUser(1l, "user1");
        Groups group1 = Groups.createGroups(user1, "group1", "group1입니다.");

        Schedule schedule1 = createSchedule(1l, "schedule1");
        group1.addSchedule(schedule1);

        Schedule schedule2 = createSchedule(2l, "schedule2");
        group1.addSchedule(schedule2);

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group1));
        when(groupsRepository.findByUserAndGroup(any(), any())).thenReturn(Optional.of(group1.getUserGroups().get(0)));
        doNothing().when(groupsRepository).downGroupUserCount(any());

        when(groupsRepository.findGroupWithSchedule(any())).thenReturn(group1);
        doNothing().when(groupsRepository).delete(any());

        //when
        groupService.exitGroup(user1, group1.getId());

        //then
        assertThat(group1.getSchedules().size()).isEqualTo(0);
    }

    /**
     * 내부 스케줄과 베팅이 모두 삭제 되어야 함
     */
    @Test
    @DisplayName("그룹 삭제")
    public void deleteGroup() {
        //given
        Users user1 = createUser(1l, "user1");
        Groups group1 = Groups.createGroups(user1, "group1", "group1입니다.");

        Schedule schedule1 = createSchedule(1l, "schedule1");
        group1.addSchedule(schedule1);

        Schedule schedule2 = createSchedule(2l, "schedule2");
        group1.addSchedule(schedule2);

        when(groupsRepository.findGroupWithSchedule(any())).thenReturn(group1);
        doNothing().when(groupsRepository).delete(any());

        //when
        groupService.deleteGroup(group1.getId());

        //then
        assertThat(group1.getSchedules().size()).isEqualTo(0);
    }

    //==뷰 조회==
    @Test
    @DisplayName("그룹 상세 조회")
    void findGroupDetail() {
        //given
        Users user = createUser(1L, "user1");
        Groups group = Groups.createGroups(user, "group1", "그룹1");
        UserGroup userGroup = group.getUserGroups().get(0);

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any())).thenReturn(Optional.of(userGroup));
        when(groupsRepository.findUserGroupWithUser(any())).thenReturn(List.of(userGroup));

        //when
        GroupDetailServiceDto groupResponse = groupService.findGroupDetail(user, group.getId());

        //then
        assertThat(groupResponse.getGroupId()).isEqualTo(group.getId());
        assertThat(groupResponse.getGroupName()).isEqualTo(group.getGroupName());
        assertThat(groupResponse.getDescription()).isEqualTo(group.getDescription());

        UserSimpleServiceDto userResponse = groupResponse.getUsers().get(0);
        assertThat(userResponse.getUserId()).isEqualTo(user.getId());
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        assertThat(userResponse.getPoint()).isEqualTo(user.getPoint());
    }

    @Test
    @DisplayName("그룹 목록 조회")
    void findGroupList() {
        //given
        Users user = createUser(1L, "user1!");

        Groups group1 = Groups.createGroups(user, "group1", "그룹1");

        Groups group2 = Groups.createGroups(user, "group2", "그룹2");
        List<UserGroup> userGroups = List.of(group1.getUserGroups().get(0), group2.getUserGroups().get(0));

        Schedule schedule1 = createSchedule(1l, "schedule1", 2024, 5, 1, 12, 0);
        group1.addSchedule(schedule1);

        Schedule schedule2 = createSchedule(2l, "schedule2", 2024, 6, 1, 12, 0);
        group1.addSchedule(schedule2);

        when(groupsRepository.findUserGroupWithGroup(any(Long.class))).thenReturn(userGroups);

        //when
        GroupListServiceDto response = groupService.findGroupList(user);

        //then
        assertThat(response.getUserId()).isEqualTo(user.getId());

        List<GroupSimpleServiceDto> responseData = response.getData();
        assertThat(responseData.size()).isEqualTo(2);
        assertThat(responseData.stream().map(GroupSimpleServiceDto::getGroupName)).contains(group1.getGroupName(), group2.getGroupName());
        assertThat(responseData.stream().map(GroupSimpleServiceDto::getDescription)).contains(group1.getDescription(), group2.getDescription());
        assertThat(responseData.stream().map(GroupSimpleServiceDto::getLastScheduleTime))
                .contains(LocalDateTime.of(2024, 6, 1, 12, 0));
    }

    @Test
    @DisplayName("그룹 분석-누적 시간 조회")
    void getLateAnalytics(){
        //given
        Users user1 = createUser(1L, "user1");
        Groups group = Groups.createGroups(user1, "group1", "그룹1");
        UserGroup userGroup = group.getUserGroups().get(0);

        Users user2 = createUser(2L, "user2");
        group.addUser(user2);

        Schedule schedule1 = createSchedule(1l, "스케줄1", 2024, 1, 1, 1, 30);
        group.addSchedule(schedule1);

        schedule1.addUserArrivalData(user1, LocalDateTime.of(2024, 1,1, 1, 20));
        schedule1.addUserArrivalData(user2, LocalDateTime.of(2024, 1,1, 1, 40));

        Schedule schedule2 = createSchedule(2l, "스케줄2", 2024, 2, 1, 1, 30);
        group.addSchedule(schedule2);

        schedule2.addUserArrivalData(user1, LocalDateTime.of(2024, 2,1, 1, 20));
        schedule2.addUserArrivalData(user2, LocalDateTime.of(2024, 2,1, 1, 25));


        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any())).thenReturn(Optional.of(userGroup));

        List<UserArrivalData> userArrivalDatas = group.getSchedules().stream()
                .map(Schedule::getUserArrivalDatas)
                .flatMap(List::stream)
                .toList();
        when(scheduleRepository.findUserArrivalDatasWithUserByGroupId(any())).thenReturn(userArrivalDatas);

        //when
        AnalyticsLateRatingServiceDto serviceDto = groupService.getLateAnalytics(user2, group.getId());

        //then
        List<AnalyticsLateServiceDto> data = serviceDto.getData();
        assertThat(data.stream().map(AnalyticsLateServiceDto::getTotalLateMinute)).containsExactly(-5, 20); //내림차순
        assertThat(data.stream().map((dto)-> dto.getUser().getUserId())).containsExactly(user2.getId(), user1.getId());
    }

    //==엔티티 생성 편의 메서드==
    Users createUser(Long id, String userName){
        return Users.builder()
                .id(id)
                .username(userName)
                .phoneNumber("010-1111-1111")
                .point(1000)
                .setting(new Setting(true, true, true, true, true))
                .build();
    }

    Schedule createSchedule(Long id, String scheduleName){
        return Schedule.builder()
                .id(id)
                .scheduleName(scheduleName)
                .scheduleTime(LocalDateTime.of(2024, 6,4, 1, 30))
                .build();
    }

    Schedule createSchedule(Long id, String scheduleName, int year, int month, int day, int hour, int minute){
        return Schedule.builder()
                .id(id)
                .scheduleName(scheduleName)
                .scheduleTime(LocalDateTime.of(year, month,day, hour, minute))
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