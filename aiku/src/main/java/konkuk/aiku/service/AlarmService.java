package konkuk.aiku.service;

import konkuk.aiku.controller.dto.EmojiMessageDto;
import konkuk.aiku.controller.dto.RealTimeLocationDto;
import konkuk.aiku.domain.*;
import konkuk.aiku.event.ScheduleEventPublisher;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.exception.TokenException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.firebase.FcmToken;
import konkuk.aiku.firebase.FcmTokenProvider;
import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.firebase.dto.RealTimeLocationMessage;
import konkuk.aiku.firebase.dto.SendingEmojiMessage;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
    private final ScheduleRepository scheduleRepository;
    private final UsersRepository usersRepository;
    private final FcmTokenProvider fcmTokenProvider;
    private final MessageSender messageSender;

    private final ScheduleEventPublisher scheduleEventPublisher;

    @Transactional
    public void saveToken(Users user, FcmToken fcmToken){
        String token = fcmToken.getToken();
        if (user.getFcmToken() != null) {
            throw new TokenException(ErrorCode.DUPLICATE_FCM_TOKEN);
        }
        fcmTokenProvider.validateFcmToken(token);
        user.setFcmToken(token);
    }

    @Transactional
    public void updateToken(Users user, FcmToken fcmToken){
        String token = fcmToken.getToken();
        if (user.getFcmToken() == null) {
            throw new TokenException(ErrorCode.NO_GENERATED_TOKEN);
        }
        fcmTokenProvider.validateFcmToken(token);
        user.setFcmToken(token);
    }

    public void sendLocationInSchedule(Users user, Long scheduleId, RealTimeLocationDto locationDto) {
        Schedule schedule = findScheduleById(scheduleId);

        checkUserInSchedule(user.getId(), scheduleId);
        checkIsScheduleRun(schedule);

        List<String> receiverToken = new ArrayList<>();
        for (UserSchedule userSchedule : schedule.getUsers()) {
            Users scheUser = userSchedule.getUser();
            if(scheUser.getId() != user.getId()){
                receiverToken.add(scheUser.getFcmToken());
            }
        }

        Map<String, String> messageDataMap = RealTimeLocationMessage.createMessage(user, locationDto.getLatitude(), locationDto.getLongitude())
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, receiverToken);

        //유저가 도착했을 시 이벤트 발행
        checkUserArrival(user, schedule, locationDto);
    }

    public void sendEmojiToUser(Users user, Long scheduleId, EmojiMessageDto emojiMessageDto){
        Schedule schedule = findScheduleById(scheduleId);
        Users receiver = findUserById(emojiMessageDto.getReceiverId());

        checkUserInSchedule(user.getId(), scheduleId);
        checkUserInSchedule(receiver.getId(), scheduleId);
        checkIsScheduleRun(schedule);

        Map<String, String> messageDataMap = SendingEmojiMessage.createMessage(user, receiver, emojiMessageDto.getEmojiType())
                .toStringMap();

        messageSender.sendMessageToUser(messageDataMap, receiver.getFcmToken());
    }

    //==유저 검증 메서드==
    private void checkUserArrival(Users user, Schedule schedule, RealTimeLocationDto locationDto){
        Double distance = locationDto.distance(schedule.getLocation().getLatitude(), schedule.getLocation().getLongitude());
        if(distance < 0.001){
            scheduleEventPublisher.userArriveInSchedule(user, schedule);
        }
    }

    private UserSchedule checkUserInSchedule(Long userId, Long scheduleId){
        UserSchedule userSchedule = scheduleRepository.findUserScheduleByUserIdAndScheduleId(userId, scheduleId).orElse(null);
        if (userSchedule == null) {
            throw new NoAthorityToAccessException(ErrorCode.NO_ATHORITY_TO_ACCESS);
        }
        return userSchedule;
    }

    private void checkIsScheduleRun(Schedule schedule){
        if(schedule.getStatus() == ScheduleStatus.WAIT){
            throw new NoAthorityToAccessException(ErrorCode.SCHEDULE_TO_WAIT);
        } else if (schedule.getStatus() == ScheduleStatus.TERM) {
            throw new NoAthorityToAccessException(ErrorCode.SCHEDULE_TO_TERM);
        }
    }

    //==레파지토리 조회 메서드==
    private Schedule findScheduleById(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }

    private Users findUserById(Long userId){
        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_USER);
        }
        return user;
    }
}
