package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.controller.dto.SettingAlarmDto;
import konkuk.aiku.controller.dto.UserAddDto;
import konkuk.aiku.controller.dto.UserUpdateDto;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.NoSuchEntityException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    EntityManager em;

    UserAddDto userAddDTO;

    @BeforeEach
    void setUp() {
        userAddDTO = new UserAddDto(
                "abcd", "010-0000-0000", 100L,
                true, true, true,
                true, true
        );
    }

    @Test
    void save() {
        Users entity = userAddDTO.toEntity();
        Long id = userService.save(entity);

        log.info("save id = {}", id);

        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void findByKakaoId() {
        Users entity = userAddDTO.toEntity();
        Long saveId = userService.save(entity);
        em.flush();

        Users byKakaoId = userService.findByKakaoId(100L);

        Assertions.assertThat(saveId).isEqualTo(byKakaoId.getId());
    }

    @Test
    void logout() {
        Users entity = userAddDTO.toEntity();
        Long saveId = userService.save(entity);
        em.flush();
        em.clear();

        Users byId = userService.findById(saveId);
        byId.setRefreshToken("abcd");
        log.info("refreshToken before = {}", byId.getRefreshToken());
        em.flush();
        em.clear();

        userService.logout(saveId);
        log.info("refreshToken after = {}", byId.getRefreshToken());
        em.flush();
        em.clear();

        byId = userService.findById(saveId);

        Assertions.assertThat(byId.getRefreshToken()).isNull();
    }

    @Test
    void updateUser() {
        UserUpdateDto userUpdateDTO = new UserUpdateDto();
        userUpdateDTO.setUserTitleId(null);
        userUpdateDTO.setUsername("가나다");
//        userService.updateUser(save, userUpdateDTO);

        // 칭호 생성 로직 작성 후 진행

    }

    @Test
    void deleteUsers() {
        Users entity = userAddDTO.toEntity();
        Long saveId = userService.save(entity);
        em.flush();

        Users users = userService.findById(saveId);
        userService.deleteUsers(users);

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchEntityException.class, () -> userService.findById(saveId));
    }

    @Test
    void setAlarm() {
        Users entity = userAddDTO.toEntity();
        Long saveId = userService.save(entity);
        em.flush();

        Users users = userService.findById(saveId);
        SettingAlarmDto settingAlarmDto = SettingAlarmDto.builder()
                .isBettingAlarmOn(false)
                .isScheduleAlarmOn(true)
                .isPinchAlarmOn(false)
                .build();
        userService.setAlarm(users, settingAlarmDto);

        Assertions.assertThat(users.getSetting().isBettingAlarmOn()).isFalse();
        Assertions.assertThat(users.getSetting().isScheduleAlarmOn()).isTrue();
        Assertions.assertThat(users.getSetting().isPinchAlarmOn()).isFalse();
    }

    @Test
    void setAuthority() {
    }

    @Test
    void getAlarm() {
    }

    @Test
    void getAuthority() {
    }

    @Test
    void getUserItems() {
    }

    @Test
    void getUserTitle() {
    }
}