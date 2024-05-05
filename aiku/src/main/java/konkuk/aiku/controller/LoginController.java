package konkuk.aiku.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import konkuk.aiku.dto.RefreshTokenDTO;
import konkuk.aiku.dto.UserSignInDTO;
import konkuk.aiku.security.JwtToken;
import konkuk.aiku.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody UserSignInDTO userSignInDTO) {
        String kakaoId = userSignInDTO.getKakaoId();
        JwtToken jwtToken = userService.signIn(kakaoId);

        return jwtToken;
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestBody UserSignInDTO userSignInDTO, @CookieValue("refreshToken") String refreshToken) {
        // Cookie -> RefreshToken 받아서
        // DB상의 Refresh 토큰과 동일 && 만료되지 않았는지 확인 -> 재발급
        String kakaoId = userSignInDTO.getKakaoId();

        return userService.refresh(kakaoId, refreshToken);
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
