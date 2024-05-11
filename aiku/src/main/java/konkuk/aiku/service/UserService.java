package konkuk.aiku.service;

import konkuk.aiku.controller.dto.SettingAlarmDto;
import konkuk.aiku.controller.dto.SettingAuthorityDto;
import konkuk.aiku.controller.dto.UserUpdateDto;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(Users users) {
        users.setPassword(passwordEncoder.encode(users.getKakaoId().toString()));
        return usersRepository.save(users).getId();
    }

    @Transactional
    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    @Transactional
    public Users findByKakaoId(Long kakaoId) {
        return usersRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    @Transactional
    public void logout(Long kakaoId) {
        Users user = findByKakaoId(kakaoId);
        user.setRefreshToken(null);
    }

    @Transactional
    public Long updateUser(Users users, UserUpdateDto userUpdateDTO) {
        users.updateUser(userUpdateDTO);
        return users.getId();
    }

    @Transactional
    public Long deleteUsers(Users users) {
        Long userId = users.getId();
        usersRepository.delete(users);
        return userId;
    }

    @Transactional
    public Long setAlarm(Users users, SettingAlarmDto settingAlarmDTO) {
        Setting setting = settingAlarmDTO.toSetting(users.getSetting());
        users.updateSetting(setting);
        return users.getId();
    }

    public Long setAuthority(Users users, SettingAuthorityDto settingAuthorityDTO) {
        Setting setting = settingAuthorityDTO.toSetting(users.getSetting());
        users.updateSetting(setting);
        return users.getId();
    }

    public Setting getAlarm(Users users) {
        return users.getSetting();
    }

    public Setting getAuthority(Users users) {
        return users.getSetting();
    }
}
