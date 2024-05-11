package konkuk.aiku.service;

import konkuk.aiku.controller.dto.UserAddDto;
import konkuk.aiku.controller.dto.UserUpdateDto;
import konkuk.aiku.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserServiceTest {

    @Autowired
    UserService userService;

    UserAddDto userAddDTO;
    Users save;

    @BeforeEach
    void setUp() {
        userAddDTO = new UserAddDto(
                "abcd", "010-0000-0000", 1L,
                true, true, true,
                true, true
        );

    }

    @Test
    void save() {
        Users entity = userAddDTO.toEntity();
//        save = userService.save(entity);

        Assertions.assertThat(save.getKakaoId()).isEqualTo(entity.getKakaoId());
    }

    @Test
    void findByKakaoId() {
        Users entity = userAddDTO.toEntity();
//        save = userService.save(entity);
        Users byKakaoId = userService.findByKakaoId(1L);

        Assertions.assertThat(userAddDTO.getKakaoId()).isEqualTo(byKakaoId.getKakaoId());
    }

    @Test
    void logout() {
        userService.logout(save.getKakaoId());

        Users findUser = userService.findByKakaoId(save.getKakaoId());

        Assertions.assertThat(findUser.getRefreshToken()).isNull();
    }

    @Test
    void updateUser() {
        UserUpdateDto userUpdateDTO = new UserUpdateDto();
        userUpdateDTO.setUserTitleId(null);
        userUpdateDTO.setUsername("가나다");
        userService.updateUser(save, userUpdateDTO);

        // 칭호 생성 로직 작성 후 진행

    }

    @Test
    void deleteUsers() {
    }

    @Test
    void setAlarm() {
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
}