package konkuk.aiku.service;

import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserGroup;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.service.dto.GroupDetailServiceDto;
import konkuk.aiku.service.dto.GroupServiceDto;
import konkuk.aiku.service.dto.UserSimpleServiceDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    GroupsRepository groupsRepository;

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
    void deleteGroup() {
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
    }

    @Test
    @DisplayName("그룹 상세 조회")
    void findGroupDetailById() {
        Users user = createUser(1L, "user1!");
        Groups group = Groups.createGroups(user, "group1", "그룹1");
        UserGroup userGroup = group.getUserGroups().get(0);

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));
        when(groupsRepository.findByUserAndGroup(any(Users.class), any(Groups.class))).thenReturn(Optional.of(userGroup));

        //when
        GroupDetailServiceDto groupResponse = groupService.findGroupDetailById(user, group.getId());

        //then
        assertThat(groupResponse.getGroupName()).isEqualTo(group.getGroupName());
        assertThat(groupResponse.getDescription()).isEqualTo(group.getDescription());

        UserSimpleServiceDto userResponse = groupResponse.getUsers().get(0);
        assertThat(userResponse.getUserId()).isEqualTo(user.getId());
        assertThat(userResponse.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("그룹 입장")
    void enterGroup() {
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
    void exitGroup() {
        Users user = createUser(1L, "user1");
        Groups group = Groups.createGroups(user, "group1", "그룹1");

        when(groupsRepository.findById(any())).thenReturn(Optional.of(group));

        //when
        Long groupId = groupService.exitGroup(user, group.getId());

        //then
        assertThat(groupId).isEqualTo(group.getId());
        assertThat(group.getUserGroups().size()).isEqualTo(0);
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