package konkuk.aiku.controller;

import jakarta.validation.Valid;
import konkuk.aiku.controller.dto.EmojiMessageDto;
import konkuk.aiku.controller.dto.RealTimeLocationDto;
import konkuk.aiku.controller.dto.SuccessResponseDto;
import konkuk.aiku.domain.Users;
import konkuk.aiku.firebase.FcmToken;
import konkuk.aiku.security.UserAdaptor;
import konkuk.aiku.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static konkuk.aiku.controller.dto.SuccessResponseDto.SuccessMessage.*;

@Controller
@RequestMapping("/alarm")
@Slf4j
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/token")
    public ResponseEntity<SuccessResponseDto> tokenSave(@RequestBody @Valid FcmToken fcmToken,
                                                        @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        alarmService.saveToken(user, fcmToken);
        return SuccessResponseDto.getResponseEntity(user.getId(), ADD_SUCCESS, HttpStatus.OK);
    }

    @PatchMapping("/token")
    public ResponseEntity<SuccessResponseDto> tokenUpdate(@RequestBody @Valid FcmToken fcmToken,
                                                        @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        alarmService.updateToken(user, fcmToken);
        return SuccessResponseDto.getResponseEntity(user.getId(), MODIFY_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/schedules/{scheduleId}/location")
    public void realTimeLocation(@PathVariable Long scheduleId,
                                 @RequestBody @Valid RealTimeLocationDto realTimeLocationDto,
                                 @AuthenticationPrincipal UserAdaptor userAdaptor){
        Users user = userAdaptor.getUsers();
        realTimeLocationDto.setId(user.getId());

        alarmService.sendLocationInSchedule(user, scheduleId, realTimeLocationDto);
    }

    @PostMapping("/schedules/{scheduleId}/emoji")
    public void emojiSend(@PathVariable Long scheduleId,
                          @RequestBody @Valid EmojiMessageDto emojiMessageDto,
                          @AuthenticationPrincipal UserAdaptor userAdaptor) {
        Users user = userAdaptor.getUsers();
        alarmService.sendEmojiToUser(user, scheduleId, emojiMessageDto);
    }
}
