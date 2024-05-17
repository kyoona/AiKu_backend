package konkuk.aiku.service;

import konkuk.aiku.controller.dto.BettingAddDto;
import konkuk.aiku.controller.dto.BettingModifyDto;
import konkuk.aiku.domain.BettingType;
import konkuk.aiku.domain.Groups;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.BettingServiceDto;
import konkuk.aiku.service.dto.LocationServiceDto;
import konkuk.aiku.service.dto.ScheduleServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class BettingServiceTest {

    @Autowired
    BettingService bettingService;

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    GroupService groupService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    GroupsRepository groupsRepository;

    Users userA1;
    Users userA2;
    Users userB;

    Groups groupA;
    Long scheduleId;

    @BeforeEach
    void setUp() {
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

        ScheduleServiceDto scheduleServiceDTO = ScheduleServiceDto.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDto(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();

        scheduleId = scheduleService.addSchedule(userA1, groupA.getId(), scheduleServiceDTO);
    }

    @Test
    void findBetting() {
    }

    @Test
    void addBetting() {
        BettingAddDto bettingAddDto = BettingAddDto.builder()
                .targetUserId(userB.getId())
                .point(50)
                .bettingType(BettingType.BETTING)
                .build();

        Long bettingId = bettingService.addBetting(userA1, scheduleId, bettingAddDto.toServiceDto());
        BettingServiceDto betting = bettingService.findBetting(bettingId);

        assertThat(betting.getBettor().getUserId()).isEqualTo(userA1.getId());
        assertThat(betting.getTargetUser().getUserId()).isEqualTo(userB.getId());
        assertThat(betting.getPoint()).isEqualTo(50);
    }

    @Test
    void updateBetting() {
        BettingAddDto bettingAddDto = BettingAddDto.builder()
                .targetUserId(userB.getId())
                .point(50)
                .bettingType(BettingType.BETTING)
                .build();

        Long bettingId = bettingService.addBetting(userA1, scheduleId, bettingAddDto.toServiceDto());

        BettingModifyDto bettingModifyDto = BettingModifyDto.builder()
                .targetUserId(userA2.getId())
                .point(70)
                .build();

        Long updateId = bettingService.updateBetting(userA1, scheduleId, bettingId, bettingModifyDto.toServiceDto());
        BettingServiceDto betting = bettingService.findBetting(updateId);

        log.info("betting={}", betting);
        assertThat(betting.getTargetUser().getUserId()).isEqualTo(userA2.getId());
        assertThat(betting.getPoint()).isEqualTo(70);

    }

    @Test
    void deleteBetting() {
        BettingAddDto bettingAddDto = BettingAddDto.builder()
                .targetUserId(userB.getId())
                .point(50)
                .bettingType(BettingType.BETTING)
                .build();

        Long bettingId = bettingService.addBetting(userA1, scheduleId, bettingAddDto.toServiceDto());

        bettingService.deleteBetting(userA1, scheduleId, bettingId);
        Assertions.assertThrows(NoSuchEntityException.class, () -> bettingService.findBetting(bettingId));
    }

    @Test
    void getBettingsByType() {
        BettingAddDto bettingA1toB = BettingAddDto.builder()
                .targetUserId(userB.getId())
                .point(50)
                .bettingType(BettingType.BETTING)
                .build();

        BettingAddDto bettingA1toA2 = BettingAddDto.builder()
                .targetUserId(userA2.getId())
                .point(50)
                .bettingType(BettingType.BETTING)
                .build();

        BettingAddDto racingA1toA2 = BettingAddDto.builder()
                .targetUserId(userA2.getId())
                .point(50)
                .bettingType(BettingType.RACING)
                .build();

        BettingAddDto racingA1toB = BettingAddDto.builder()
                .targetUserId(userB.getId())
                .point(50)
                .bettingType(BettingType.RACING)
                .build();

        BettingAddDto racingA2toB = BettingAddDto.builder()
                .targetUserId(userB.getId())
                .point(50)
                .bettingType(BettingType.RACING)
                .build();

        bettingService.addBetting(userA1, scheduleId, bettingA1toA2.toServiceDto());
        bettingService.addBetting(userA1, scheduleId, bettingA1toB.toServiceDto());
        bettingService.addBetting(userA1, scheduleId, racingA2toB.toServiceDto());
        bettingService.addBetting(userA1, scheduleId, racingA1toA2.toServiceDto());
        bettingService.addBetting(userA1, scheduleId, racingA1toB.toServiceDto());

        List<BettingServiceDto> bettingList = bettingService.getBettingsByType(scheduleId, "BETTING");
        List<BettingServiceDto> racingList = bettingService.getBettingsByType(scheduleId, "RACING");

        log.info("betting={}", bettingList);
        log.info("racing={}", racingList);

        assertThat(bettingList.size()).isEqualTo(2);
        assertThat(racingList.size()).isEqualTo(3);
    }
}