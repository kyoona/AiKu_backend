package konkuk.aiku.service;

import konkuk.aiku.controller.dto.RealTimeLocationDto;
import konkuk.aiku.domain.*;
import konkuk.aiku.exception.NoAthorityToAccessException;
import konkuk.aiku.exception.NoSuchEntityException;
import konkuk.aiku.exception.TokenException;
import konkuk.aiku.exception.ErrorCode;
import konkuk.aiku.firebase.FcmToken;
import konkuk.aiku.firebase.FcmTokenProvider;
import konkuk.aiku.firebase.MessageSender;
import konkuk.aiku.repository.ScheduleRepository;
import konkuk.aiku.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AlarmService {
    private final UsersRepository usersRepository;
    private final ScheduleRepository scheduleRepository;
    private final FcmTokenProvider fcmTokenProvider;
    private final MessageSender messageSender;

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
        Schedule schedule = findBySchedule(scheduleId);

        checkIsScheduleRun(schedule);
        checkUserInSchedule(user.getId(), scheduleId);

        List<String> receiverToken = new ArrayList<>();
        for (UserSchedule userSchedule : schedule.getUsers()) {
            Users scheUser = userSchedule.getUser();
            if(scheUser.getId() != user.getId()){
                receiverToken.add(scheUser.getFcmToken());
            }
        }

        messageSender.sendRealTimeLocation(user.getId(), locationDto.getLatitude(), locationDto.getLongitude(), receiverToken);
    }

    private Schedule findBySchedule(Long scheduleId){
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if (schedule == null) {
            throw new NoSuchEntityException(ErrorCode.NO_SUCH_SCHEDULE);
        }
        return schedule;
    }

    private UserSchedule checkUserInSchedule(Long userId, Long scheduleId){
        UserSchedule userSchedule = scheduleRepository.findByUserIdAndScheduleId(userId, scheduleId).orElse(null);
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
}
