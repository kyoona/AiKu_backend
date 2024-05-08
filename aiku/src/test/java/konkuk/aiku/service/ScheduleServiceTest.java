package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.LocationServiceDTO;
import konkuk.aiku.service.dto.ScheduleDetailServiceDTO;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
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
                .personName("user1")
                .kakaoId("kakao1")
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
        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);

        ScheduleServiceDTO scheduleServiceDTO2 = ScheduleServiceDTO.builder()
                .scheduleName("schedule2")
                .location(new LocationServiceDTO(100.1, 100.1, "aiku"))
                .scheduleTime(LocalDateTime.now())
                .build();
        scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO2);

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
                .personName("user1")
                .kakaoId("kakao1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        //when
        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
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
                .personName("user1")
                .kakaoId("kakao1")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        ScheduleServiceDTO scheduleServiceDTO2 = ScheduleServiceDTO.builder()
                .scheduleName("modify")
                .location(null) //수정되면 안됨(값이 없는 필드는 수정 x)
                .scheduleTime(null)
                .build();
        scheduleService.modifySchedule(user, group.getId(), scheduleId, scheduleServiceDTO2);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDTO2.getScheduleName());

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
                .personName("user1")
                .kakaoId("kakao1")
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .personName("user2")
                .kakaoId("kakao2")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        ScheduleServiceDTO scheduleServiceDTO2 = ScheduleServiceDTO.builder()
                .scheduleName("modify")
                .location(null)
                .scheduleTime(null)
                .build();
        assertThatThrownBy(() -> scheduleService.modifySchedule(user2, group.getId(), scheduleId, scheduleServiceDTO2)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 삭제")
    public void deleteSchedule() {
        //given
        Users user = Users.builder()
                .personName("user1")
                .kakaoId("kakao1")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
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
                .personName("user1")
                .kakaoId("kakao1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .personName("user2")
                .kakaoId("kakao2")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        ScheduleDetailServiceDTO scheduleDetailServiceDTO = scheduleService.findScheduleDetailById(user, group.getId(), scheduleId);

        //then
        assertThat(scheduleDetailServiceDTO.getId()).isEqualTo(scheduleId);
        assertThat(scheduleDetailServiceDTO.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
        assertThat(scheduleDetailServiceDTO.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());

        assertThat(scheduleDetailServiceDTO.getAcceptUsers().get(0).getUsername()).isEqualTo(user.getUsername());
        assertThat(scheduleDetailServiceDTO.getAcceptUsers().get(0).getUserKaKaoId()).isEqualTo(user.getKakaoId());

        assertThat(scheduleDetailServiceDTO.getWaitUsers().get(0).getUsername()).isEqualTo(user2.getUsername());
        assertThat(scheduleDetailServiceDTO.getWaitUsers().get(0).getUserKaKaoId()).isEqualTo(user2.getKakaoId());
    }

    @Test
    @Commit
    @DisplayName("스케줄 참가")
    public void enterSchedule() {
        //given
        Users user = Users.builder()
                .personName("user1")
                .kakaoId("kakao1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .personName("user2")
                .kakaoId("kakao2")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
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
                .personName("user1")
                .kakaoId("kakao1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .personName("user2")
                .kakaoId("kakao2")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
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
                .personName("user1")
                .kakaoId("kakao1")
                .build();
        usersRepository.save(user);

        Groups group = Groups.builder()
                .groupName("group1")
                .description("group1입니다")
                .build();
        groupsRepository.save(group);

        //whe
        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 퇴장")
    public void exitSchedule() {
        //given
        Users user = Users.builder()
                .personName("user1")
                .kakaoId("kakao1")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
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
                .personName("user1")
                .kakaoId("kakao1")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(user);

        Users user2 = Users.builder()
                .personName("user2")
                .kakaoId("kakao2")
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

        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(user, group.getId(), scheduleServiceDTO);
//        em.flush();
//        em.clear();

        //when
        assertThatThrownBy(() -> scheduleService.exitSchedule(user2, group.getId(), scheduleId)).isInstanceOf(NoAthorityToAccessException.class);
    }
}