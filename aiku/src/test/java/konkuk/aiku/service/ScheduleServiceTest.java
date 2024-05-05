package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.repository.GroupsRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UserGroupRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.LocationServiceDTO;
import konkuk.aiku.service.dto.ScheduleServiceDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

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
    @DisplayName("스케줄 등록")
    public void addSchedule() throws Exception{
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
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

        //when
        ScheduleServiceDTO scheduleServiceDTO = ScheduleServiceDTO.builder()
                .scheduleName("schedule1")
                .location(new LocationServiceDTO(127.1, 127.1, "Konkuk University"))
                .scheduleTime(LocalDateTime.now())
                .build();
        Long scheduleId = scheduleService.addSchedule(userKaKaoId1, group.getId(), scheduleServiceDTO);

        ScheduleServiceDTO scheduleServiceDTO2 = ScheduleServiceDTO.builder()
                .scheduleName("schedule2")
                .location(new LocationServiceDTO(100.1, 100.1, "aiku"))
                .scheduleTime(LocalDateTime.now())
                .build();
        scheduleService.addSchedule(userKaKaoId1, group.getId(), scheduleServiceDTO2);

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
    public void addScheduleInFaultCondition() throws Exception{
        //given
        String userKaKaoId1 = "kakao1";
        Users user = Users.builder()
                .username("user1")
                .kakaoId(userKaKaoId1)
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

        assertThatThrownBy(() -> scheduleService.addSchedule(userKaKaoId1, group.getId(), scheduleServiceDTO)).isInstanceOf(NoAthorityToAccessException.class);
    }
}