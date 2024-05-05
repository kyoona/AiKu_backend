package konkuk.aiku.service;

import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;

    @Transactional
    public Users save(Users users) {
        return usersRepository.save(users);
    }

    @Transactional
    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    @Transactional
    public Users findByKakaoId(String kakaoId) {
        return usersRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    @Transactional
    public void logout(String kakaoId) {
        Users user = findByKakaoId(kakaoId);
        user.setRefreshToken(null);
    }

}
