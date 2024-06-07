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
import konkuk.aiku.repository.BettingRepository;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BettingRepository bettingRepository;
    private final FcmTokenProvider fcmTokenProvider;
    private final MessageSender messageSender;

    private final ScheduleEventPublisher scheduleEventPublisher;
    private final BettingEventPublisher bettingEventPublisher;


    @Transactional
    public void saveToken(Users user, FcmToken fcmToken){
        Users findUser = findUserById(user.getId());
        log.info("findUser = {} : {}", findUser.getUsername(), findUser.getId());
        String token = fcmToken.getToken();
        if (findUser.getFcmToken() != null) {
            throw new TokenException(ErrorCode.DUPLICATE_FCM_TOKEN);
        }
        findUser.setFcmToken(token);
    }

    @Transactional
    public void updateToken(Users user, FcmToken fcmToken){
        Users findUser = findUserById(user.getId());
        String token = fcmToken.getToken();
        if (findUser.getFcmToken() == null) {
            throw new TokenException(ErrorCode.NO_GENERATED_TOKEN);
        }
        findUser.setFcmToken(token);
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

    public void sendScheduleMapOpen(Long scheduleId){
        Schedule schedule = findScheduleWithUser(scheduleId);

        List<String> userTokens = getScheduleUsersFcmToken(schedule);

        Map<String, String> messageDataMap = ScheduleMessage.createMessage(MessageTitle.SCHEDULE_MAP_OPEN, schedule)
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
    }

    //==이벤트 서비스==
    public Runnable sendScheduleFinishRunnable(Long scheduleId){
        return () -> {
            Schedule schedule = findScheduleWithUser(scheduleId);

            List<String> userTokens = getScheduleUsersFcmToken(schedule);

            Map<String, String> messageDataMap = ScheduleMessage.createMessage(MessageTitle.SCHEDULE_FINISH, schedule)
                    .toStringMap();
            messageSender.sendMessageToUsers(messageDataMap, userTokens);
        };
    }

    public Runnable sendNextScheduleRunnable(Long scheduleId) {
        return () -> {
            Schedule schedule = findScheduleWithUser(scheduleId);

            List<String> userTokens = getScheduleUsersFcmToken(schedule);

            Map<String, String> messageDataMap = ScheduleMessage.createMessage(MessageTitle.NEXT_SCHEDULE, schedule)
                    .toStringMap();
            messageSender.sendMessageToUsers(messageDataMap, userTokens);
        };
    }

    public void sendUserArrival(Long userId, Long scheduleId, LocalDateTime arrivalTime){
        Users user = findUserById(userId);
        Schedule schedule = findScheduleWithUser(scheduleId);

        List<String> userTokens = getScheduleUsersFcmToken(schedule);


        Map<String, String> messageDataMap = UserArrivalMessage.createMessage(user, schedule, arrivalTime)
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
    }

    public void sendEmojiToUser(Users user, Long scheduleId, EmojiMessageDto emojiMessageDto){
        Schedule schedule = findScheduleById(scheduleId);
        Users receiver = findUserById(emojiMessageDto.getReceiverId());

        checkUserInSchedule(user.getId(), scheduleId);
        checkUserInSchedule(receiver.getId(), scheduleId);
        checkIsScheduleRun(schedule);

        Map<String, String> messageDataMap = SendingEmojiMessage.createMessage(user, scheduleId, receiver, emojiMessageDto)
                .toStringMap();

        messageSender.sendMessageToUser(messageDataMap, receiver.getFcmToken());
    }

    public void sendScheduleMapClose(Long scheduleId){
        Schedule schedule = findScheduleWithUser(scheduleId);

        List<String> userTokens = getScheduleUsersFcmToken(schedule);

        Map<String, String> messageDataMap = ScheduleMessage.createMessage(MessageTitle.SCHEDULE_MAP_CLOSE, schedule)
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
    }

    public void sendBettingNew(Long bettingId){
        Betting betting = findBettingWithUserAndSchedule(bettingId);

        List<String> userTokens = getBettingUsersFcmToken(betting);

        Map<String, String> messageDataMap = BettingMessage.createMessage(MessageTitle.BETTING_NEW, betting.getSchedule(), betting, betting.getBettor(), betting.getTargetUser())
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
    }

    public void sendBettingAcceptOrDeny(Long bettingId, boolean isAccept){
        Betting betting = findBettingWithUserAndSchedule(bettingId);
        Schedule schedule = betting.getSchedule();

        List<String> userTokens = null;
        MessageTitle messageTitle = null;

        if (isAccept){
            userTokens = getScheduleUsersFcmToken(schedule);
            messageTitle = MessageTitle.BETTING_ACCEPT;
        }else{
            userTokens = getBettingUsersFcmToken(betting);
            messageTitle = MessageTitle.BETTING_DENY;
        }

        Map<String, String> messageDataMap = BettingAcceptDenyMessage.createMessage(messageTitle, schedule, betting, betting.getBettor(), betting.getTargetUser(), isAccept)
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
    }

    //TODO 스케줄에 참여한 유저 전원에게 알림
    public void sendBettingStart(Long bettingId){
        Betting betting = findBettingWithUserAndSchedule(bettingId);

        List<String> userTokens = getBettingUsersFcmToken(betting);

        Map<String, String> messageDataMap = BettingMessage
                .createMessage(MessageTitle.BETTING_START, betting.getSchedule(), betting, betting.getBettor(), betting.getTargetUser())
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
    }

    //TODO 스케줄에 참여한 유저 전원에게 알림
    public void sendBettingFinish(Long bettingId){
        Betting betting = findBettingWithUserAndSchedule(bettingId);

        List<String> userTokens = getBettingUsersFcmToken(betting);

        Map<String, String> messageDataMap = BettingMessage
                .createMessage(MessageTitle.BETTING_FINISH, betting.getSchedule(), betting, betting.getBettor(), betting.getTargetUser())
                .toStringMap();
        messageSender.sendMessageToUsers(messageDataMap, userTokens);
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

    private Betting findBettingWithUserAndSchedule(Long bettingId) {
        Betting betting = bettingRepository.findBettingWithUserAndSchedule(bettingId).orElse(null);
        if (betting == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_BETTING);
        }
        return betting;
    }


    //==편의 메서드==
    private static List<String> getScheduleUsersFcmToken(Schedule schedule) {
        List<String> receiverTokens = schedule.getUsers().stream()
                .map(UserSchedule::getUser)
                .map(Users::getFcmToken)
                .toList();
        return receiverTokens;
    }

    private static List<String> getBettingUsersFcmToken(Betting betting) {
        return List.of(betting.getBettor().getFcmToken(), betting.getTargetUser().getFcmToken());
    }
}
