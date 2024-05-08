package konkuk.aiku;

import konkuk.aiku.controller.dto.UserAddDTO;
import konkuk.aiku.domain.Users;
import konkuk.aiku.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserTest {

    @Autowired
    UserService userService;

    @Test
    @Transactional
    void userSaveTest() {
        UserAddDTO userAddDTO = new UserAddDTO(
                "abcd", "010-0000-0000", "0001",
                true, true, true,
                true, true
        );
        Users entity = userAddDTO.toEntity();

        Users save = userService.save(entity);

        Users byKakaoId = userService.findByKakaoId("0001");

        Assertions.assertThat(save).isEqualTo(byKakaoId);
    }
}
