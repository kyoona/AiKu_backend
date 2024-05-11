package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.AccessTokenDto;
import konkuk.aiku.controller.dto.UserSignInDto;
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
    public JwtToken signIn(@RequestBody UserSignInDto userSignInDTO) {
        Long kakaoId = userSignInDTO.getKakaoId();
        JwtToken jwtToken = userLoginService.signIn(kakaoId);

        return jwtToken;
    }

    @PostMapping("/refresh")
    public AccessTokenDto refreshToken(@RequestBody UserSignInDto userSignInDTO, @CookieValue("refreshToken") String refreshToken) {
        // Cookie -> RefreshToken 받아서
        // DB상의 Refresh 토큰과 동일 && 만료되지 않았는지 확인 -> 재발급
        Long kakaoId = userSignInDTO.getKakaoId();
        String accessToken = userLoginService.refresh(kakaoId, refreshToken);

        return new AccessTokenDto(kakaoId, accessToken);
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
