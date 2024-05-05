package konkuk.aiku.controller;

import konkuk.aiku.domain.Users;
import konkuk.aiku.dto.UserAddDTO;
import konkuk.aiku.service.UserLoginService;
import konkuk.aiku.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserAddDTO save(@RequestBody UserAddDTO userAddDTO) {
        Users user = userService.save(userAddDTO.toEntity());
        return UserAddDTO.toDto(user);
    }

    @PostMapping("/logout")
    public void logout(String kakaoId) {
        userService.logout(kakaoId);
    }

}
