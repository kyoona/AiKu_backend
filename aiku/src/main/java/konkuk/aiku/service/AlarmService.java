package konkuk.aiku.service;

import konkuk.aiku.controller.dto.EmojiMessageDto;
import konkuk.aiku.controller.dto.RealTimeLocationDto;
import konkuk.aiku.domain.*;
import konkuk.aiku.event.BettingEventPublisher;
import konkuk.aiku.event.ScheduleEventPublisher;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.exception.TokenException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.firebase.FcmToken;
import konkuk.aiku.firebase.FcmTokenProvider;
import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.firebase.dto.*;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private final BettingEventPublisher bettingEventPublisher;


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

    public void receiveRealTimeLocation(Users user, Long scheduleId, RealTimeLocationDto locationDto) {
        Schedule schedule = findScheduleWithUser(scheduleId);

        checkUserInSchedule(user.getId(), scheduleId);
        checkIsScheduleRun(schedule);

        List<String> receiverTokens = getScheduleUsersFcmToken(schedule);

        Map<String, String> messageDataMap = RealTimeLocationMessage
                .createMessage(user, scheduleId, locationDto.getLatitude(), locationDto.getLongitude())
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, receiverTokens);
    }

    public void receiveUserArrival(Users user, Long scheduleId, LocalDateTime arrivalTime){
        Schedule schedule = findScheduleWithUser(scheduleId);

        checkUserInSchedule(user.getId(), scheduleId);
        checkIsScheduleRun(schedule);

        List<String> receiverTokens = getScheduleUsersFcmToken(schedule);

        Map<String, String> messageDataMap = UserArrivalMessage
                .createMessage(user, schedule, arrivalTime)
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, receiverTokens);

        //유저가 도착했을 때 실행되어야 될 것들
        scheduleEventPublisher.userArriveInScheduleEvent(user.getId(), scheduleId, arrivalTime);
        bettingEventPublisher.userArriveInBettingEvent(user, schedule);
    }

    //==이벤트 서비스==
    public Runnable sendStartScheduleRunnable(Long scheduleId){
        return () -> {
            Schedule schedule = findScheduleWithUser(scheduleId);

            List<String> userTokens = getScheduleUsersFcmToken(schedule);

            Map<String, String> messageDataMap = ScheduleAlarmMessage.createMessage(MessageTitle.START_SCHEDULE, schedule)
                    .toStringMap();
            messageSender.sendMessageToUsers(messageDataMap, userTokens);
        };
    }

    public Runnable sendNextScheduleRunnable(Long scheduleId) {
        return () -> {
            Schedule schedule = findScheduleWithUser(scheduleId);

            List<String> userTokens = getScheduleUsersFcmToken(schedule);

            Map<String, String> messageDataMap = ScheduleAlarmMessage.createMessage(MessageTitle.NEXT_SCHEDULE, schedule)
                    .toStringMap();
            messageSender.sendMessageToUsers(messageDataMap, userTokens);
        };
    }

    public void sendUserArrival(Long userId, Long scheduleId, LocalDateTime arrivalTime){
        
    }

    public void sendEmojiToUser(Users user, Long scheduleId, EmojiMessageDto emojiMessageDto){
        Schedule schedule = findScheduleById(scheduleId);
        Users receiver = findUserById(emojiMessageDto.getReceiverId());

        checkUserInSchedule(user.getId(), scheduleId);
        checkUserInSchedule(receiver.getId(), scheduleId);
        checkIsScheduleRun(schedule);

        Map<String, String> messageDataMap = SendingEmojiMessage.createMessage(user, scheduleId, receiver, emojiMessageDto.getEmojiType())
                .toStringMap();

        messageSender.sendMessageToUser(messageDataMap, receiver.getFcmToken());
    }

    //==유저 검증 메서드==
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

    private Schedule findScheduleWithUser(Long scheduleID){
        Schedule schedule = scheduleRepository.findScheduleWithUser(scheduleID).orElse(null);
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

    //==편의 메서드==
    public Long getScheduleAlarmTimeDelay(Long scheduleId){
        Schedule schedule = findScheduleById(scheduleId);
        LocalDateTime scheduleTime = schedule.getScheduleTime();

        return Duration.between(LocalDateTime.now(), scheduleTime).toSeconds();
    }

    private static List<String> getScheduleUsersFcmToken(Schedule schedule) {
        List<String> receiverTokens = schedule.getUsers().stream()
                .map(UserSchedule::getUser)
                .map(Users::getFcmToken)
                .toList();
        return receiverTokens;
    }
}
