package konkuk.aiku;

import konkuk.aiku.domain.Users;
import konkuk.aiku.dto.UserAddDTO;
import konkuk.aiku.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    UserService userService;

    @Test
    void userSaveTest() {
        UserAddDTO userAddDTO = new UserAddDTO(
                "abcd", "010-0000-0000", "0001",
                true, true, true,
                true, true
        );
        Users entity = userAddDTO.toEntity();
        System.out.println(entity);

        Users save = userService.save(entity);
        System.out.println(save);
    }
}
