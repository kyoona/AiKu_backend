package konkuk.aiku.service;

import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.security.JwtToken;
import konkuk.aiku.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UsersRepository usersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;


    // username + password로 인증용 토큰 생성 -> 일단 카카오 아이디로만 설정
    @Transactional
    public JwtToken signIn(String kakaoId) {

        UsernamePasswordAuthenticationToken authenticationFilter
                = new UsernamePasswordAuthenticationToken(kakaoId, kakaoId);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationFilter);

        return jwtTokenProvider.generateToken(authentication);
    }

}
