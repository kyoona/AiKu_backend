package konkuk.aiku.controller;

import konkuk.aiku.dto.UserSignInDTO;
import konkuk.aiku.security.JwtToken;
import konkuk.aiku.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody UserSignInDTO userSignInDTO) {
        String kakaoId = userSignInDTO.getKakaoId();
        JwtToken jwtToken = userService.signIn(kakaoId);
        log.info("kakaoId = {}", kakaoId);
        log.info("accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
