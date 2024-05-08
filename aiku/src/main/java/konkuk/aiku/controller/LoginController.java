package konkuk.aiku.controller;

import konkuk.aiku.security.JwtToken;
import konkuk.aiku.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserLoginService userLoginService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody konkuk.aiku.dto.UserSignInDto userSignInDTO) {
        Long kakaoId = userSignInDTO.getKakaoId();
        JwtToken jwtToken = userLoginService.signIn(kakaoId);

        return jwtToken;
    }

    @PostMapping("/refresh")
    public konkuk.aiku.dto.AccessTokenDto refreshToken(@RequestBody konkuk.aiku.dto.UserSignInDto userSignInDTO, @CookieValue("refreshToken") String refreshToken) {
        // Cookie -> RefreshToken 받아서
        // DB상의 Refresh 토큰과 동일 && 만료되지 않았는지 확인 -> 재발급
        Long kakaoId = userSignInDTO.getKakaoId();
        String accessToken = userLoginService.refresh(kakaoId, refreshToken);

        return new konkuk.aiku.dto.AccessTokenDto(kakaoId, accessToken);
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
