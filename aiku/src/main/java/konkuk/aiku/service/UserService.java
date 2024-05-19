package konkuk.aiku.service;

import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.*;
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
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final TitleRepository titleRepository;

    private final UsersRepository usersRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserItemRepository userItemRepository;
    private final PasswordEncoder passwordEncoder;

    public Long save(UserServiceDto userServiceDto) {
        Users users = userServiceDto.toEntity();
        users.setPassword(passwordEncoder.encode(users.getKakaoId().toString()));
        return usersRepository.save(users).getId();
    }

    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_ENTITY));
    }

    public Users findByKakaoId(Long kakaoId) {
        return usersRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
    }

    public Long logout(Users users) {
        users.setRefreshToken(null);
        return users.getId();
    }

    public Long updateUser(Users users, UserServiceDto userServiceDto) {
        users.setUsername(userServiceDto.getUsername());
        users.updateUserImg(userServiceDto.getUserImg(), userServiceDto.getUserImgData());

        return users.getId();
    }

    public UserTitle getUserTitle(Long mainTitleId) {
        return userTitleRepository.findByUserTitleId(mainTitleId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_TITLE));
    }

    public Long deleteUsers(Users users) {
        Long userId = users.getId();
        usersRepository.delete(users);
        return userId;
    }

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

    public List<ItemResponseDto> getUserItems(Users users, String itemCategory) {
        // 성능 고려 안하고 짜서 리팩토링 필요!!!!
        ItemCategory category = ItemCategory.valueOf(itemCategory);
        List<UserItem> userItems = userItemRepository.findUserItemsByUserIdAndItemItemCategory(users.getId(), category);

        return userItems.stream()
                .map(UserItem::getItem)
                .map(ItemResponseDto::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public List<TitleResponseDto> getUserTitles(Long userId) {
        return userTitleRepository.findByUserId(userId).stream()
                .map(TitleResponseDto::toDto)
                .collect(Collectors.toList());
    }

    public Long addUserTitle(Users users, Long titleId) {
        Title title = findTitleById(titleId);
        UserTitle userTitle = UserTitle.builder()
                .user(users)
                .title(title)
                .build();

        userTitleRepository.save(userTitle);
        users.addTitle(userTitle);

        return userTitle.getId();
    }

    private Title findTitleById(Long titleId) {
        return titleRepository.findById(titleId)
                .orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_TITLE));
    }

    public Long updateMainTitle(Users users, Long userTitleId) {
        UserTitle userTitle = getUserTitle(userTitleId);
        users.setMainTitle(userTitle);
        return users.getId();
    }
}
