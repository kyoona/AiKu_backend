package konkuk.aiku.service;

import jakarta.persistence.EntityManager;
import konkuk.aiku.controller.dto.SettingAlarmDto;
import konkuk.aiku.controller.dto.TitleDto;
import konkuk.aiku.controller.dto.UserAddDto;
import konkuk.aiku.controller.dto.UserUpdateDto;
import konkuk.aiku.domain.UserImgData;
import konkuk.aiku.domain.UserTitle;
import konkuk.aiku.domain.Users;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.service.dto.UserServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
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
    AdminService adminService;
    @Autowired
    EntityManager em;

    UserAddDto userAddDTO;

    @BeforeEach
    void setUp() {
        UserImgData userImgData = UserImgData.builder()
                .imgData(UserImgData.ImgType.DEFAULT1)
                .colorCode("AABBCC")
                .build();
        userAddDTO = new UserAddDto(
                "abcd", "010-0000-0000", 100L,
                "", userImgData,true, true, true,
                true, true
        );
    }

    @Test
    void save() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long id = userService.save(userServiceDto);

        log.info("save id = {}", id);

        Assertions.assertThat(id).isNotNull();
    }

    @Test
    void findByKakaoId() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long saveId = userService.save(userServiceDto);

        Users byKakaoId = userService.findByKakaoId(100L);

        Assertions.assertThat(saveId).isEqualTo(byKakaoId.getId());
    }

    @Test
    void logout() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long saveId = userService.save(userServiceDto);

        Users byId = userService.findById(saveId);
        byId.setRefreshToken("abcd");
        log.info("refreshToken before = {}", byId.getRefreshToken());

        userService.logout(saveId);
        log.info("refreshToken after = {}", byId.getRefreshToken());

        byId = userService.findById(saveId);

        Assertions.assertThat(byId.getRefreshToken()).isNull();
    }

    @Test
    void updateUser() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long saveId = userService.save(userServiceDto);
        Users users = userService.findById(saveId);
        log.info("colorCodeBefore={}",users.getUserImgData().getColorCode());

        UserUpdateDto userUpdateDTO = new UserUpdateDto();
        userUpdateDTO.setUsername("가나다");
        userUpdateDTO.setUserImg("");

        UserImgData changeImgData = UserImgData.builder()
                .colorCode("bbccdd")
                .imgData(UserImgData.ImgType.DEFAULT2)
                .build();
        userUpdateDTO.setUserImgData(changeImgData);

        UserServiceDto userUpdateServiceDto = userUpdateDTO.toServiceDto();
        userService.updateUser(users, userUpdateServiceDto);

        log.info("colorCodeAfter={}",users.getUserImgData().getColorCode());

        Assertions.assertThat(users.getUserImgData().getColorCode()).isEqualTo("bbccdd");
        Assertions.assertThat(users.getUsername()).isEqualTo("가나다");
    }

    @Test
    void updateUserTitle() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long saveId = userService.save(userServiceDto);
        Users users = userService.findById(saveId);

        TitleDto testTitleA = new TitleDto();
        testTitleA.setTitleName("테스트타이틀A");
        testTitleA.setDescription("테스트용 타이틀입니다.");
        TitleDto testTitleB = new TitleDto();
        testTitleB.setTitleName("테스트타이틀B");
        testTitleB.setDescription("테스트용 타이틀입니다.");

        Long titleAId = adminService.addTitle(testTitleA.toServiceDto());
        Long userTitleAId = userService.addUserTitle(users, titleAId);
        Long titleBId = adminService.addTitle(testTitleB.toServiceDto());
        Long userTitleBId = userService.addUserTitle(users, titleBId);
        log.info("userTitleBId={}",userTitleBId);


        UserTitle userTitleBefore = userService.getUserTitle(userTitleAId);
        users.setMainTitle(userTitleBefore);
        log.info("userTitleBefore={}",users.getMainTitle().getTitle().getTitleName());

        userService.updateMainTitle(users, userTitleBId);

        log.info("userTitleAfter={}",users.getMainTitle().getTitle().getTitleName());

        Assertions.assertThat(users.getMainTitle().getTitle().getTitleName()).isEqualTo("테스트타이틀B");

    }

    @Test
    void deleteUsers() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long saveId = userService.save(userServiceDto);
        em.flush();

        Users users = userService.findById(saveId);
        userService.deleteUsers(users);

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchEntityException.class, () -> userService.findById(saveId));
    }

    @Test
    void setAlarm() {
        UserServiceDto userServiceDto = userAddDTO.toServiceDto();
        Long saveId = userService.save(userServiceDto);
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