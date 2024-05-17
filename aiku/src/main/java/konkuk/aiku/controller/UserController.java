package konkuk.aiku.controller;

import konkuk.aiku.controller.dto.*;
import konkuk.aiku.domain.Setting;
import konkuk.aiku.domain.Users;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.UserService;
import konkuk.aiku.service.dto.UserServiceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<SuccessResponseDto> save(@RequestBody UserAddDto userAddDTO) {
        Long userId = userService.save(userAddDTO.toServiceDto());
        return SuccessResponseDto.getResponseEntity(userId, ADD_SUCCESS, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUsers(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        UserResponseDto userDto = UserResponseDto.toDto(users);
        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<SuccessResponseDto> updateUsers(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestBody UserUpdateDto userUpdateDTO) {
        Users users = userAdaptor.getUsers();
        UserServiceDto userServiceDto = userUpdateDTO.toServiceDto();

        Long userId = userService.updateUser(users, userServiceDto);
        return SuccessResponseDto.getResponseEntity(userId, MODIFY_SUCCESS, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponseDto> deleteUsers(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        Long userId = userService.deleteUsers(users);
        return SuccessResponseDto.getResponseEntity(userId, DELETE_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/titles/{titleId}")
    public ResponseEntity<SuccessResponseDto> addUserTitle(@AuthenticationPrincipal UserAdaptor userAdaptor, @PathVariable Long titleId) {
        Users users = userAdaptor.getUsers();
        Long userTitleId = userService.addUserTitle(users, titleId);
        return SuccessResponseDto.getResponseEntity(userTitleId, ADD_SUCCESS, HttpStatus.OK);
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

    @GetMapping("/items")
    public ResponseEntity<List<ItemResponseDto>> getUserItems(@AuthenticationPrincipal UserAdaptor userAdaptor, @RequestParam String itemType) {
        Users users = userAdaptor.getUsers();
        List<ItemResponseDto> itemList = userService.getUserItems(users, itemType);

        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @GetMapping("/titles")
    public ResponseEntity<List<TitleResponseDto>> getUserTitles(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users users = userAdaptor.getUsers();
        List<TitleResponseDto> titleList = userService.getUserTitles(users.getId());

        return new ResponseEntity<>(titleList, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessResponseDto> logout(@AuthenticationPrincipal UserAdaptor userAdaptor) {
        String kakaoId = userAdaptor.getUsername();
        Long userId = userService.logout(Long.valueOf(kakaoId));

        return SuccessResponseDto.getResponseEntity(userId, LOGOUT_SUCCESS, HttpStatus.OK);
    }

}
