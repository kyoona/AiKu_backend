package konkuk.aiku.service;

import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.security.JwtToken;
import konkuk.aiku.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserLoginService {
    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    // username + password로 인증용 토큰 생성 -> 일단 카카오 아이디로만 설정
    @Transactional
    public JwtToken signIn(Long kakaoId) {

        UsernamePasswordAuthenticationToken authenticationFilter
                = new UsernamePasswordAuthenticationToken(kakaoId, kakaoId);

        Authentication authentication = null;

        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        Optional<Users> user = usersRepository.findByKakaoId(kakaoId);
        if (user.isPresent()) {
            user.get().setRefreshToken(jwtToken.getRefreshToken());
        }

        return jwtToken;
    }

    // username + password로 인증용 토큰 생성 -> 일단 카카오 아이디로만 설정
    @Transactional
    public String refresh(Long kakaoId, String refreshToken) {

        String refreshDBToken = usersRepository.findByKakaoId(kakaoId)
                .map(user -> user.getRefreshToken())
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));

        if (!refreshDBToken.equals(refreshToken)) {
            throw new SecurityException("Invalid JWT Token");
        }

        UsernamePasswordAuthenticationToken authenticationFilter
                = new UsernamePasswordAuthenticationToken(kakaoId, kakaoId);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationFilter);

        return jwtTokenProvider.refreshAccessToken(authentication);
    }

}
