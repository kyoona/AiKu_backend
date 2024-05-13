package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.*;
import konkuk.aiku.exception.AlreadyInException;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.LocationServiceDto;
import konkuk.aiku.service.dto.ScheduleDetailServiceDto;
import konkuk.aiku.service.dto.ScheduleServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    GroupService groupService;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    GroupsRepository groupsRepository;

    Users userA1;
    Users userA2;
    Users userB;

    Groups groupA;

    @BeforeEach
    void beforeEach(){
        userA1 = Users.builder()
                .username("userA1")
                .phoneNumber("010-1111-1111")
                .userImg("http://userA1.img")
                .point(1000)
                .fcmToken("fcm")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userA1);

        userA2 = Users.builder()
                .username("userA2")
                .phoneNumber("010-1111-2222")
                .userImg("http://userA2.img")
                .point(2000)
                .fcmToken("fcm")
                .setting(new Setting(true, true, true, true, true))
                .build();
        usersRepository.save(userA2);

        userB = Users.builder()
                .username("userB")
                .phoneNumber("010-3333-3333")
                .userImg("http://userB.img")
                .point(3000)
                .fcmToken("fcm")
                .setting(new Setting(false, false, false, false, false))
                .build();
        usersRepository.save(userB);

        groupA = Groups.createGroups(userA1, "groupA", "groupA입니다.");
        groupsRepository.save(groupA);

        groupService.enterGroup(userA2, groupA.getId());
    }

    @Test
    @Commit
    @DisplayName("스케줄 등록")
    public void addSchedule() {
        //when
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
        assertThat(schedule.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());
        assertThat(schedule.getLocation().getLocationName()).isEqualTo(scheduleServiceDTO.getLocation().getLocationName());
        assertThat(schedule.getLocation().getLatitude()).isEqualTo(scheduleServiceDTO.getLocation().getLatitude());
        assertThat(schedule.getLocation().getLongitude()).isEqualTo(scheduleServiceDTO.getLocation().getLongitude());
    }

    @Test
    @DisplayName("스케줄 등록-그룹에 속해 있지 않은 유저")
    public void addScheduleInFaultCondition() {
        //when
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();

        assertThatThrownBy(() -> scheduleService.addSchedule(userB, groupA.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 수정")
    public void modifySchedule() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        ScheduleServiceDto scheduleServiceDto2 = ScheduleServiceDto.builder()
                .scheduleName("modify")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        scheduleService.modifySchedule(userA1, groupA.getId(), scheduleId, scheduleServiceDto2);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDto2.getScheduleName());

        assertThat(schedule.getScheduleTime()).isNotNull();
        assertThat(schedule.getScheduleTime()).isEqualTo(scheduleServiceDto2.getScheduleTime());

        assertThat(schedule.getLocation()).isNotNull();
        assertThat(schedule.getLocation().getLocationName()).isEqualTo(scheduleServiceDto2.getLocation().getLocationName());
        assertThat(schedule.getLocation().getLatitude()).isEqualTo(scheduleServiceDto2.getLocation().getLatitude());
        assertThat(schedule.getLocation().getLongitude()).isEqualTo(scheduleServiceDto2.getLocation().getLongitude());
    }

    @Test
    @DisplayName("스케줄 수정-스케줄에 속해 있지 않은 유저")
    public void modifyScheduleInFaultCondition() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        ScheduleServiceDto scheduleServiceDto2 = ScheduleServiceDto.builder()
                .scheduleName("modify")
                .location(null)
                .scheduleTime(null)
                .build();
        assertThatThrownBy(() -> scheduleService.modifySchedule(userA2, groupA.getId(), scheduleId, scheduleServiceDto2)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 삭제")
    public void deleteSchedule() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        scheduleService.deleteSchedule(userA1, groupA.getId(), scheduleId);

        //then
        assertThat(scheduleRepository.findById(scheduleId)).isEmpty();
    }

    @Test
    @DisplayName("스케줄 상세 조회")
    public void findScheduleDetailById() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        ScheduleDetailServiceDto scheduleDetailServiceDTO = scheduleService.findScheduleDetailById(userA1, groupA.getId(), scheduleId);

        //then
        assertThat(scheduleDetailServiceDTO.getId()).isEqualTo(scheduleId);
        assertThat(scheduleDetailServiceDTO.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
        assertThat(scheduleDetailServiceDTO.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());

        assertThat(scheduleDetailServiceDTO.getAcceptUsers().get(0).getUsername()).isEqualTo(userA1.getUsername());
        assertThat(scheduleDetailServiceDTO.getAcceptUsers().get(0).getUserId()).isEqualTo(userA1.getId());

        assertThat(scheduleDetailServiceDTO.getWaitUsers().get(0).getUsername()).isEqualTo(userA2.getUsername());
        assertThat(scheduleDetailServiceDTO.getWaitUsers().get(0).getUserId()).isEqualTo(userA2.getId());
    }

    @Test
    @Commit
    @DisplayName("스케줄 참가")
    public void enterSchedule() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        scheduleService.enterSchedule(userA2, groupA.getId(), scheduleId);

        //then
        assertThat(scheduleRepository.findUserScheduleByUserIdAndScheduleId(userA2.getId(), scheduleId)).isNotEmpty();
    }

    @Test
    @DisplayName("스케줄 참가-이미 참여중인 유저")
    public void enterScheduleNotInGroup() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        assertThatThrownBy(() -> scheduleService.enterSchedule(userA1, groupA.getId(), scheduleId)).isInstanceOf(AlreadyInException.class);
    }

    @Test
    @DisplayName("스케줄 참가-그룹에 속해 있지 않은 유저")
    public void enterScheduleAlreadyIn() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();

        //when
        assertThatThrownBy(() -> scheduleService.addSchedule(userB, groupA.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 퇴장")
    public void exitSchedule() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        scheduleService.exitSchedule(userA1, groupA.getId(), scheduleId);

        //then
        assertThat(scheduleRepository.findUserScheduleByUserIdAndScheduleId(userA1.getId(), scheduleId)).isEmpty();
    }

    @Test
    @DisplayName("스케줄 퇴장-참여하지 않은 유저")
    public void exitScheduleNotIn() {
        //given
        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        assertThatThrownBy(() -> scheduleService.exitSchedule(userA2, groupA.getId(), scheduleId)).isInstanceOf(NoAthorityToAccessException.class);
    }
}
