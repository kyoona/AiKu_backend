package konkuk.aiku.service;

import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.ItemCategory;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.UserTitle;
import konkuk.aiku.domain.Users;
import konkuk.aiku.domain.userItem.UserItem;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.repository.TitleRepository;
import konkuk.aiku.repository.UserItemRepository;
import konkuk.aiku.repository.UserTitleRepository;
import konkuk.aiku.repository.UsersRepository;
import konkuk.aiku.service.dto.UserServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserItemRepository userItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long save(Users users) {
        users.setPassword(passwordEncoder.encode(users.getKakaoId().toString()));
        return usersRepository.save(users).getId();
    }

    @Transactional
    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_ENTITY));
    }

    @Transactional
    public Users findByKakaoId(Long kakaoId) {
        return usersRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    @Transactional
    public Long logout(Long userId) {
        Users user = findById(userId);
        user.setRefreshToken(null);
        return user.getId();
    }

    @Transactional
    public Long updateUser(Users users, UserServiceDto userServiceDto) {
        users.setUsername(userServiceDto.getUsername());
        UserTitle userTitle = userTitleRepository.findByUserTitleId(userServiceDto.getMainTitle().getId())
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_TITLE));
        users.setMainTitle(userTitle);

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

    @Transactional
    public Long setAuthority(Users users, SettingAuthorityDto settingAuthorityDTO) {
        Setting setting = settingAuthorityDTO.toSetting(users.getSetting());
        users.updateSetting(setting);
        return users.getId();
    }

    @Transactional
    public Setting getAlarm(Users users) {
        return users.getSetting();
    }

    @Transactional
    public Setting getAuthority(Users users) {
        return users.getSetting();
    }

    @Transactional
    public List<ItemResponseDto> getUserItems(Users users, String itemCategory) {
        // 성능 고려 안하고 짜서 리팩토링 필요!!!!
        ItemCategory category = ItemCategory.valueOf(itemCategory);
        List<UserItem> userItems = userItemRepository.findUserItemsByUserIdAndItemItemCategory(users.getId(), category);

        return userItems.stream()
                .map(UserItem::getItem)
                .map(ItemResponseDto::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public List<TitleResponseDto> getUserTitle(Long userId) {
        return userTitleRepository.findByUserId(userId).stream()
                .map(TitleResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
