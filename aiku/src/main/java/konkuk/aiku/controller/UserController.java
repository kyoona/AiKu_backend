package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> save(@RequestBody UserAddDto userAddDTO) {
        Long userId = userService.save(userAddDTO.toEntity());
        return SuccessResponseDto.getResponseEntity(userId, ADD_SUCCESS, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUsers(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        UserResponseDto userDto = UserResponseDto.toDto(users);
        return new ResponseEntity<UserResponseDto>(userDto, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<SuccessResponseDto> updateUsers(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody UserUpdateDto userUpdateDTO) {
        Users users = userAdaptor.getUsers();
        Long userId = userService.updateUser(users, userUpdateDTO);
        return SuccessResponseDto.getResponseEntity(userId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponseDto> deleteUsers(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        Long userId = userService.deleteUsers(users);
        return SuccessResponseDto.getResponseEntity(userId, DELETE_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/setting/alarm")
    public ResponseEntity<SuccessResponseDto> setAlarm(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody SettingAlarmDto settingAlarmDTO) {
        Users users = userAdaptor.getUsers();
        Long userId = userService.setAlarm(users, settingAlarmDTO);
        return SuccessResponseDto.getResponseEntity(userId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/setting/alarm")
    public ResponseEntity<SettingAlarmDto> getAlarm(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        Setting alarm = userService.getAlarm(users);
        SettingAlarmDto alarmDto = SettingAlarmDto.toDto(alarm);
        return new ResponseEntity<>(alarmDto, HttpStatus.OK);
    }

    @PatchMapping("/setting/authority")
    public ResponseEntity<SuccessResponseDto> setAuthority(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody SettingAuthorityDto settingAuthorityDTO) {
        Users users = userAdaptor.getUsers();
        Long userId = userService.setAuthority(users, settingAuthorityDTO);
        return SuccessResponseDto.getResponseEntity(userId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/setting/authority")
    public ResponseEntity<SettingAuthorityDto> getAuthority(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        Setting alarm = userService.getAuthority(users);
        SettingAuthorityDto authDto = SettingAuthorityDto.toDto(alarm);
        return new ResponseEntity<>(authDto, HttpStatus.OK);
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
