package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserAddDto save(@RequestBody UserAddDto userAddDTO) {
        Users user = userService.save(userAddDTO.toEntity());
        return UserAddDto.toDto(user);
    }

    @GetMapping
    public UserResponseDto getUsers(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        return UserResponseDto.toDto(users);
    }

    @PatchMapping
    public void updateUsers(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody UserUpdateDto userUpdateDTO) {
        Users users = userAdaptor.getUsers();
        userService.updateUser(users, userUpdateDTO);
    }

    @DeleteMapping
    public void deleteUsers(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        userService.deleteUsers(users);
    }

    @PatchMapping("/setting/alarm")
    public void setAlarm(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody SettingAlarmDto settingAlarmDTO) {
        Users users = userAdaptor.getUsers();
        userService.setAlarm(users, settingAlarmDTO);
    }

    @GetMapping("/setting/alarm")
    public SettingAlarmDto getAlarm(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        Setting alarm = userService.getAlarm(users);
        return SettingAlarmDto.toDto(alarm);
    }

    @PatchMapping("/setting/authority")
    public void setAuthority(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody SettingAuthorityDto settingAuthorityDTO) {
        Users users = userAdaptor.getUsers();
        userService.setAuthority(users, settingAuthorityDTO);
    }

    @GetMapping("/setting/authority")
    public SettingAuthorityDto getAuthority(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        Setting alarm = userService.getAuthority(users);
        return SettingAuthorityDto.toDto(alarm);
    }

    @PostMapping("/image")
    public void image(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        // S3 이미지 등록 로직
    }



    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        String kakaoId = userAdaptor.getUsername();
        userService.logout(Long.valueOf(kakaoId));
    }

}
