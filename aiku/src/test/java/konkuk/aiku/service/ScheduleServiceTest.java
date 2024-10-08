package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.controller.dto.ScheduleCond;
import konkuk.aiku.domain.*;
import konkuk.aiku.exception.AlreadyInException;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    GroupService groupService;

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    GroupsRepository groupsRepository;

    Users userA1, userA2, userB;
    Groups groupA, groupB;

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
                .phoneNumber("010-2222-2222")
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

        groupB = Groups.createGroups(userB, "groupB", "groupB입니다.");
        groupsRepository.save(groupB);

        groupService.enterGroup(userA2, groupA.getId());
    }

    @AfterEach
    void afterEach(){
        scheduleRepository.deleteAll();
        groupsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @Commit
    @DisplayName("스케줄 등록")
     void addSchedule() {
        //when
        ScheduleServiceDto scheduleServiceDTO = createScheduleDto("schedule1");
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
        assertThat(schedule.getLocation().getLocationName()).isEqualTo(scheduleServiceDTO.getLocation().getLocationName());
        assertThat(schedule.getLocation().getLatitude()).isEqualTo(scheduleServiceDTO.getLocation().getLatitude());
        assertThat(schedule.getLocation().getLongitude()).isEqualTo(scheduleServiceDTO.getLocation().getLongitude());
    }

    @Test
    @DisplayName("스케줄 등록-그룹에 속해 있지 않은 유저")
     void addScheduleInFaultCondition() {
        //when
        ScheduleServiceDto scheduleServiceDTO = createScheduleDto("schedule1");

        assertThatThrownBy(() -> scheduleService.addSchedule(userB, groupA.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @DisplayName("스케줄 수정")
     void modifySchedule() {
        //given
        ScheduleServiceDto scheduleServiceDTO = createScheduleDto("schedule1");
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        ScheduleServiceDto scheduleServiceDto2 = createScheduleDto("modify");
        scheduleService.modifySchedule(userA1, groupA.getId(), scheduleId, scheduleServiceDto2);

        //then
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        assertThat(schedule.getScheduleName()).isEqualTo(scheduleServiceDto2.getScheduleName());
        assertThat(schedule.getScheduleTime()).isEqualTo(scheduleServiceDto2.getScheduleTime());
        assertThat(schedule.getLocation().getLocationName()).isEqualTo(scheduleServiceDto2.getLocation().getLocationName());
        assertThat(schedule.getLocation().getLatitude()).isEqualTo(scheduleServiceDto2.getLocation().getLatitude());
        assertThat(schedule.getLocation().getLongitude()).isEqualTo(scheduleServiceDto2.getLocation().getLongitude());
    }

    @Test
    @DisplayName("스케줄 수정-스케줄에 속해 있지 않은 유저")
     void modifyScheduleInFaultCondition() {
        //given
        ScheduleServiceDto scheduleServiceDTO = createScheduleDto("schedule1");
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        //when
        ScheduleServiceDto scheduleServiceDto2 = createScheduleDto("modify");
        assertThatThrownBy(() -> scheduleService.modifySchedule(userA2, groupA.getId(), scheduleId, scheduleServiceDto2)).isInstanceOf(NoAthorityToAccessException.class);
    }

    @Test
    @Commit
    @DisplayName("스케줄 참가")
    void enterSchedule() {
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
    void enterScheduleNotInGroup() {
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
    void enterScheduleAlreadyIn() {
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
    @DisplayName("스케줄 퇴장-참여하지 않은 유저")
    void exitScheduleNotIn() {
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

    //==뷰 조회==
    @Test
    @DisplayName("스케줄 상세 조회")
    @Commit
     void findScheduleDetail() {
        //given
        ScheduleServiceDto scheduleServiceDTO = createScheduleDto("schedule1");
        Long scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        ScheduleDetailServiceDto response = scheduleService.findScheduleDetail(userA1, groupA.getId(), scheduleId);

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        assertThat(response.getId()).isEqualTo(scheduleId);
        assertThat(response.getScheduleName()).isEqualTo(scheduleServiceDTO.getScheduleName());
//        assertThat(response.getScheduleTime()).isEqualTo(scheduleServiceDTO.getScheduleTime());
        assertThat(response.getLocation().getLatitude()).isEqualTo(scheduleServiceDTO.getLocation().getLatitude());
        assertThat(response.getLocation().getLongitude()).isEqualTo(scheduleServiceDTO.getLocation().getLongitude());
        assertThat(response.getLocation().getLocationName()).isEqualTo(scheduleServiceDTO.getLocation().getLocationName());

        assertThat(response.getAcceptUsers().get(0).getUserId()).isEqualTo(userA1.getId());
        assertThat(response.getAcceptUsers().get(0).getUsername()).isEqualTo(userA1.getUsername());

        assertThat(response.getWaitUsers().get(0).getUserId()).isEqualTo(userA2.getId());
        assertThat(response.getWaitUsers().get(0).getUsername()).isEqualTo(userA2.getUsername());
    }

    @Test
    @DisplayName("그룹 스케줄 목록 조회")
     void findGroupScheduleList() {
        //given
        ScheduleServiceDto scheduleServiceDTO1 = createScheduleDto("schedule1");
        Long scheduleId1 = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO1);

        ScheduleServiceDto scheduleServiceDTO2 = ScheduleServiceDto.builder()
                .scheduleName("schedule2")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .status(ScheduleStatus.WAIT)
                .build();
        Long scheduleId2 = scheduleService.addSchedule(userA2, groupA.getId(), scheduleServiceDTO2);

        scheduleService.enterSchedule(userA2, groupA.getId(), scheduleId1);

        em.flush();
        em.clear();
        log.info("<given> finish");

        //when
        GroupScheduleListServiceDto serviceDto = scheduleService.findGroupScheduleList(userA1, groupA.getId(), new ScheduleCond(null, null, null));

        em.flush();
        em.clear();
        log.info("<when> finish");

        //then
        assertThat(serviceDto.getGroupId()).isEqualTo(groupA.getId());
        assertThat(serviceDto.getGroupName()).isEqualTo(groupA.getGroupName());
        assertThat(serviceDto.getWaitSchedule()).isEqualTo(2);

        List<ScheduleSimpleServiceDto> dtoData = serviceDto.getData();
        assertThat(dtoData.size()).isEqualTo(2);
        assertThat(dtoData.stream().map(ScheduleSimpleServiceDto::getScheduleId)).contains(scheduleId1, scheduleId2);
        assertThat(dtoData.stream().map(ScheduleSimpleServiceDto::getUserCount)).contains(2, 1);
        assertThat(dtoData.stream().map(ScheduleSimpleServiceDto::isAccept)).contains(true, false);
    }

    @Test
    @DisplayName("유저 스케줄 목록 조회")
     void findUserScheduleList() {
        //given
        ScheduleServiceDto scheduleServiceDTO1 = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId1 = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO1);
        scheduleService.enterSchedule(userA2, groupA.getId(), scheduleId1);

        groupService.enterGroup(userA1, groupB.getId());
        ScheduleServiceDto scheduleServiceDTO2 = ScheduleServiceDto.builder()
                .scheduleName("schedule2")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId2 = scheduleService.addSchedule(userA1, groupB.getId(), scheduleServiceDTO2);
        em.flush();
        em.clear();

        //when
        UserScheduleListServiceDto serviceDto = scheduleService.findUserScheduleList(userA1, new ScheduleCond(null, null, null));

        //then
        assertThat(serviceDto.getUserId()).isEqualTo(userA1.getId());
        assertThat(serviceDto.getUsername()).isEqualTo(userA1.getUsername());
        assertThat(serviceDto.getWaitSchedule()).isEqualTo(2);
        assertThat(serviceDto.getRunSchedule()).isEqualTo(0);
        assertThat(serviceDto.getTermSchedule()).isEqualTo(0);

        List<ScheduleSimpleServiceDto> dtoData = serviceDto.getData();
        assertThat(dtoData.size()).isEqualTo(2);
        assertThat(dtoData.stream().map(ScheduleSimpleServiceDto::getScheduleId)).contains(scheduleId1, scheduleId2);
        assertThat(dtoData.stream().map(ScheduleSimpleServiceDto::getUserCount)).contains(1, 2);
    }
    
    //==편의 메서드==
    ScheduleServiceDto createScheduleDto(String scheduleName) {
        return ScheduleServiceDto.builder()
                .scheduleName(scheduleName)
                .location(new LocationServiceDto(127.11111111111, 127.11111111111, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .status(ScheduleStatus.WAIT)
                .build();
    }
}
