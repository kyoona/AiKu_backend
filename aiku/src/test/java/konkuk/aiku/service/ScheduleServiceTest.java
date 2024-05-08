package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.LocationServiceDto;
import konkuk.aiku.service.dto.ScheduleDetailServiceDto;
import konkuk.aiku.service.dto.ScheduleServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ScheduleServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    EntityManager entityManager;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    GroupsRepository groupsRepository;
    @Autowired
    UserGroupRepository userGroupRepository;

    @Test
    @Commit
    @DisplayName("스케줄 등록")
    public void addSchedule() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);
//        em.flush();
//        em.clear();

        //when
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);

        ScheduleServiceDto scheduleServiceDto2 = ScheduleServiceDto.builder()
                .scheduleName("schedule2")
                .location(new LocationServiceDto(100.1, 100.1, "aiku"))
                .scheduleTime(LocalDateTime.now())
                .build();
        scheduleService.addSchedule(user, group.getId(), scheduleServiceDto2);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
        assertThat(schedule.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());
        assertThat(schedule.getLocation().getLocationName()).isEqualTo(scheduleServiceDTO.getLocation().getLocationName());
        assertThat(schedule.getLocation().getLatitude()).isEqualTo(scheduleServiceDTO.getLocation().getLatitude());
        assertThat(schedule.getLocation().getLongitude()).isEqualTo(scheduleServiceDTO.getLocation().getLongitude());

        List<UserSchedule> userSchedules = entityManager.createQuery("select us from UserSchedule us where us.user.id = :userId", UserSchedule.class)
                .setParameter("userId", user.getId())
                .getResultList();
        assertThat(userSchedules.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("스케줄 등록-그룹에 속해 있지 않은 유저")
    public void addScheduleInFaultCondition() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        //when
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @Commit
    @DisplayName("스케줄 수정")
    public void modifySchedule() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        ScheduleServiceDto scheduleServiceDto2 = ScheduleServiceDto.builder()
                .scheduleName("modify")
                .location(null) //수정되면 안됨(값이 없는 필드는 수정 x)
                .scheduleTime(null)
                .build();
        scheduleService.modifySchedule(user, group.getId(), scheduleId, scheduleServiceDto2);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDto2.getScheduleName());

        assertThat(schedule.getScheduleTime()).isNotNull();
        assertThat(schedule.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());

        assertThat(schedule.getLocation()).isNotNull();
        assertThat(schedule.getLocation().getLocationName()).isEqualTo(scheduleServiceDTO.getLocation().getLocationName());
        assertThat(schedule.getLocation().getLatitude()).isEqualTo(scheduleServiceDTO.getLocation().getLatitude());
        assertThat(schedule.getLocation().getLongitude()).isEqualTo(scheduleServiceDTO.getLocation().getLongitude());
    }

    @Test
    @DisplayName("스케줄 수정-스케줄에 속해 있지 않은 유저")
    public void modifyScheduleInFaultCondition() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .username("user2")
                .build();
        usersRepository.save(user2);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        ScheduleServiceDto scheduleServiceDto2 = ScheduleServiceDto.builder()
                .scheduleName("modify")
                .location(null)
                .scheduleTime(null)
                .build();
        assertThatThrownBy(() -> scheduleService.modifySchedule(user2, group.getId(), scheduleId, scheduleServiceDto2)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 삭제")
    public void deleteSchedule() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        scheduleService.deleteSchedule(user, group.getId(), scheduleId);

        //then
        assertThat(scheduleRepository.findById(scheduleId)).isEmpty();
    }

    @Test
    @DisplayName("스케줄 상세 조회")
    public void findScheduleDetailById() {
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(user2);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setUser(user2);
        userGroup2.setGroup(group);
        userGroupRepository.save(userGroup2);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        ScheduleDetailServiceDto scheduleDetailServiceDTO = scheduleService.findScheduleDetailById(user, group.getId(), scheduleId);

        //then
        assertThat(scheduleDetailServiceDTO.getId()).isEqualTo(scheduleId);
        assertThat(scheduleDetailServiceDTO.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
        assertThat(scheduleDetailServiceDTO.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());

        assertThat(scheduleDetailServiceDTO.getAcceptUsers().get(0).getUsername()).isEqualTo(user.getUsername());
        assertThat(scheduleDetailServiceDTO.getAcceptUsers().get(0).getUserId()).isEqualTo(user.getId());

        assertThat(scheduleDetailServiceDTO.getWaitUsers().get(0).getUsername()).isEqualTo(user2.getUsername());
        assertThat(scheduleDetailServiceDTO.getWaitUsers().get(0).getUserId()).isEqualTo(user2.getId());
    }

    @Test
    @Commit
    @DisplayName("스케줄 참가")
    public void enterSchedule() {
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(user2);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setUser(user2);
        userGroup2.setGroup(group);
        userGroupRepository.save(userGroup2);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        scheduleService.enterSchedule(user2, group.getId(), scheduleId);

        //then
        assertThat(scheduleRepository.findByUserIdAndScheduleId(user2.getId(), scheduleId)).isNotEmpty();
    }

    @Test
    @DisplayName("스케줄 참가-이미 참여중인 유저")
    public void enterScheduleNotInGroup() {
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(user2);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        assertThatThrownBy(() -> scheduleService.enterSchedule(user2, group.getId(), scheduleId)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 참가-그룹에 속해 있지 않은 유저")
    public void enterScheduleAlreadyIn() {
        //given
        Users user = Users.builder()
                .username("user1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        //whe
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 퇴장")
    public void exitSchedule() {
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);

        //when
        scheduleService.exitSchedule(user, group.getId(), scheduleId);

        //then
        assertThat(scheduleRepository.findByUserIdAndScheduleId(user.getId(), scheduleId)).isEmpty();
    }

    @Test
    @DisplayName("스케줄 퇴장-참여하지 않은 유저")
    public void exitScheduleNotIn() {
        //given
        Users user = Users.builder()
                .username("user1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .username("user2")
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(user2);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroupRepository.save(userGroup);

        UserGroup userGroup2 = new UserGroup();
        userGroup2.setUser(user2);
        userGroup2.setGroup(group);
        userGroupRepository.save(userGroup2);

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        assertThatThrownBy(() -> scheduleService.exitSchedule(user2, group.getId(), scheduleId)).isInstanceOf(NoAthorityToAccessException.class);
    }
}