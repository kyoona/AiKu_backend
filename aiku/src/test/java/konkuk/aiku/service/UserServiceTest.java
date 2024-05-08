package konkuk.aiku.service;

import konkuk.aiku.controller.dto.UserAddDTO;
import konkuk.aiku.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save() {
        UserAddDTO userAddDTO = new UserAddDTO(
                "abcd", "010-0000-0000", 1L,
                true, true, true,
                true, true
        );
        Users entity = userAddDTO.toEntity();

        Users save = userService.save(entity);

        Users byKakaoId = userService.findByKakaoId(1L);

        Assertions.assertThat(save).isEqualTo(byKakaoId);
    }

    @Test
    void findById() {
    }

    @Test
    void findByKakaoId() {
    }

    @Test
    void logout() {
    }

    @Test
    void updateUser() {
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