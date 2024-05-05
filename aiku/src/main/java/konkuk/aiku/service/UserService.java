package konkuk.aiku.service;

import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;

    public Users save(Users users) {
        return usersRepository.save(users);
    }

    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    public Users findByKakaoId(String kakaoId) {
        return usersRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    public void logout(String kakaoId) {
        Users user = findByKakaoId(kakaoId);
        user.setRefreshToken(null);
    }



}
